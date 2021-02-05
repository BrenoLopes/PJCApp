import { Component, OnInit } from '@angular/core';
import { Album } from '@core/services/api/albums';
import { ActivatedRoute, Router } from '@angular/router';
import { RefreshService } from '@core/services/auth/refresh/refresh.service';
import { MyStorageService } from '@core/services/storage/my-storage.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { Artist } from '@core/services/api/artists';
import {
  createMachine,
  interpret,
  Interpreter,
  MachineConfig,
  MachineOptions,
  StateMachine,
} from 'xstate';
import { GetSingleArtistService } from '@core/services/api/artists/get-single-artist.service';
import {
  GetAllAlbumsService,
  PagedAlbumResponse,
} from '@core/services/api/albums/get-all-albums.service';
import { HttpErrorResponse } from '@angular/common/http';
import {
  AddAlbumDialogComponent,
  AddReturnTypes,
} from '@shared/dialog/album/add-album-dialog/add-album-dialog.component';
import {
  DeleteAlbumDialogComponent,
  RemoveReturnTypes,
} from '@shared/dialog/album/delete-album-dialog/delete-album-dialog.component';
import {
  EditAlbumDialogComponent,
  EditReturnType,
} from '@shared/dialog/album/edit-album-dialog/edit-album-dialog.component';

@Component({
  selector: 'app-list-all',
  templateUrl: './list-all-albums.component.html',
  styleUrls: ['./list-all-albums.component.scss'],
})
export class ListAllAlbumsComponent implements OnInit {
  currentUser = '';
  listIsLoaded = false;
  theArtist!: Artist | undefined;

  private albumsMap = new Map<string, Album[]>();
  private machine!: Interpreter<{}>;

  constructor(
    public router: Router,
    private route: ActivatedRoute,
    private tokenRefreshService: RefreshService,
    private getSingleArtist: GetSingleArtistService,
    private getAllAlbums: GetAllAlbumsService,
    private localStorage: MyStorageService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((param) => {
      const artistName = param.get('artist');

      if (!artistName) {
        this.navigateToHomePage();
        return;
      }

      this.appLoaded(artistName);
    });
  }

  getMapEntries = (): [string, Album[]][] => {
    return Array.from(this.albumsMap.entries());
  }

  openAddDialog = () => {
    const dialogAddRef = this.dialog.open(AddAlbumDialogComponent, {
      data: {
        artist: this.theArtist,
      },
    });

    dialogAddRef.afterClosed().subscribe((result: AddReturnTypes) => {
      switch (result) {
        case AddReturnTypes.FAILED:
          this.snackBar.open('Falha ao adicionar o album.', 'Ok');
          return;
        case AddReturnTypes.CANCELED:
          return;
        case AddReturnTypes.CONFLICT:
          this.snackBar.open('Ja existe um album com este nome!.', 'Ok');
          return;
        case AddReturnTypes.ADDED:
          this.snackBar.open('O album foi adicionado com sucesso!', '', {
            duration: 3000,
          });

          if (!this.theArtist) {
            return;
          }

          this.appLoaded(this.theArtist.name);
          return;
        default:
      }
    });
  }

  openEditDialog(album: Album): void {
    const dialogAddRef = this.dialog.open(EditAlbumDialogComponent, {
      data: {
        album,
      },
    });

    dialogAddRef.afterClosed().subscribe((result: EditReturnType) => {
      switch (result) {
        case EditReturnType.FAILED:
          this.snackBar.open('Falha ao editar o album.', 'Ok');
          return;
        case EditReturnType.CANCELED:
          return;
        case EditReturnType.CONFLICT:
          this.snackBar.open('Ja existe um album com este nome!.', 'Ok');
          return;
        case EditReturnType.ADDED:
          this.snackBar.open('O album foi editado com sucesso!', '', {
            duration: 3000,
          });

          if (!this.theArtist) {
            return;
          }

          this.appLoaded(this.theArtist.name);
          return;
        default:
      }
    });
  }

  openDeleteDialog = (album: Album) => {
    const dialogAddRef = this.dialog.open(DeleteAlbumDialogComponent, {
      data: {
        album,
      },
    });

    dialogAddRef.afterClosed().subscribe((result: RemoveReturnTypes) => {
      switch (result) {
        case RemoveReturnTypes.FAILED:
          this.snackBar.open('Falha ao remover o album.', 'Ok');
          return;
        case RemoveReturnTypes.CANCELED:
          return;
        default:
          this.snackBar.open('O album foi removido com sucesso!', '', {
            duration: 3000,
          });

          if (!this.theArtist) {
            return;
          }

          this.appLoaded(this.theArtist.name);
      }
    });
  }

  private appLoaded(artistName: string): void {
    if (this.doesntHaveJwtToken()) {
      this.navigateToLoginPage();
    }

    const success = () => {
      this.listIsLoaded = true;
    };

    const redirect = () => {
      console.log('redirect');
    };

    const alert = (type: AlertType) => {
      console.log('Alert ', type);
    };

    const loadArtist = () => this.loadArtist(artistName);

    this.machine = interpret(
      this.buildStateMachine(
        loadArtist,
        this.loadAlbum,
        this.loadRefresh,
        success,
        redirect,
        alert
      )
    );

    this.machine.start();
  }

  private buildStateMachine(
    loadArtist: VoidFunction,
    loadAlbum: VoidFunction,
    loadRefresh: VoidFunction,
    success: VoidFunction,
    forbidden: VoidFunction,
    alert: (type: AlertType) => void
  ): StateMachine<any, any, any> {
    const config: MachineConfig<any, any, any> = {
      id: 'albumLoader',
      initial: 'artistLoader',
      states: {
        artistLoader: {
          entry: ['loadArtist'],
          on: {
            ARTIST_LOAD_SUCCESS: { target: 'albumLoader' },
            ARTIST_LOAD_FAILED: { target: 'refreshLoader' },
            ARTIST_LOAD_NOTFOUND: { target: 'alertNotFound' },
          },
        },
        albumLoader: {
          entry: ['loadAlbum'],
          on: {
            ALBUM_LOAD_SUCCESS: { target: 'success' },
            ALBUM_LOAD_FAILED: { target: 'alertRefresh' },
          },
        },
        refreshLoader: {
          entry: ['loadRefresh'],
          on: {
            REFRESH_LOAD_SUCCESS: { target: 'albumLoader' },
            REFRESH_LOAD_FAILED: { target: 'redirect' },
          },
        },
        success: { entry: ['success'], type: 'final' },
        redirect: { entry: ['forbidden'], type: 'final' },
        alertRefresh: { entry: ['alertRefresh'], type: 'final' },
        alertNotFound: { entry: ['alertNotFound'], type: 'final' },
        alertServerError: { entry: ['alertServerError'], type: 'final' },
      },
    };

    const options: Partial<MachineOptions<any, any>> = {
      actions: {
        loadArtist: () => loadArtist(),
        loadAlbum: () => loadAlbum(),
        loadRefresh: () => loadRefresh(),
        success: () => success(),
        forbidden: () => forbidden(),
        alertRefresh: () => alert(AlertType.RefreshMessage),
        alertNotFound: () => alert(AlertType.NotFound),
        alertServerError: () => alert(AlertType.ServerErrorMessage),
      },
    };

    return createMachine(config, options);
  }

  private loadArtist = (artistName: string): void => {
    const success = (response: Artist) => {
      if (!response) {
        this.machine.send('ARTIST_LOAD_NOT_FOUND');
        return;
      }

      this.theArtist = response;

      this.machine.send('ARTIST_LOAD_SUCCESS');
    };

    const failed = (e: HttpErrorResponse) => {
      if (e.status === 404) {
        this.machine.send('ARTIST_LOAD_NOT_FOUND');
        return;
      }

      this.machine.send('ARTIST_LOAD_FAILED');
    };

    this.getSingleArtist.requestArtist(artistName).subscribe(success, failed);
  }

  private loadRefresh = (): void => {
    const success = () => {
      this.machine.send('REFRESH_LOAD_SUCCESS');
    };

    const failed = () => {
      this.machine.send('ARTIST_LOAD_FAILED');
    };

    this.tokenRefreshService.refreshJwtTokenIfExpired(success, failed);
  }

  private loadAlbum = (): void => {
    if (!this.theArtist) {
      this.machine.send('ALBUM_LOAD_NOTFOUND');
      return;
    }

    const success = (response: PagedAlbumResponse) => {
      this.albumsMap.clear();
      response.albums.forEach(this.createMapOfAlbums);

      this.machine.send('ALBUM_LOAD_SUCCESS');
    };

    const failed = (e: HttpErrorResponse) => {
      this.machine.send('ALBUM_LOAD_FAILED');
    };

    this.getAllAlbums
      .requestAlbumsList(this.theArtist.id, 0, 10000, 'asc')
      .subscribe(success, failed);
  }

  private createMapOfAlbums = (album: Album): void => {
    if (album.name.length <= 0) {
      return;
    }

    const key = album.name.charAt(0).toLowerCase();
    let theAlbums = this.albumsMap.get(key);

    if (!theAlbums) {
      theAlbums = [];
    }

    this.albumsMap.set(key, [...theAlbums, album]);
  }

  private navigateToHomePage(): void {
    this.router.navigateByUrl('/').then();
  }

  private navigateToLoginPage(): void {
    this.router.navigateByUrl('/login').then();
  }

  private doesntHaveJwtToken = (): boolean => {
    return !this.localStorage.haveJwtToken();
  }
}

enum AlertType {
  RefreshMessage,
  ServerErrorMessage,
  NotFound,
}
