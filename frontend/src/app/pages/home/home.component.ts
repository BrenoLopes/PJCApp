import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { GetAllArtistsService } from '@core/services/api/artists/get-all-artists.service';
import { RefreshService } from '@core/services/auth/refresh/refresh.service';
import { MyStorageService } from '@core/services/storage/my-storage.service';
import { Artist, PagedArtistResponse } from '@core/services/api/artists';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';

import { AddArtistDialogComponent } from '../../shared/dialog/artist/add-artist-dialog/add-artist-dialog.component';
import { DeleteArtistDialogComponent } from '../../shared/dialog/artist/delete-artist-dialog/delete-artist-dialog.component';
import { UpdateArtistDialogComponent } from '../../shared/dialog/artist/update-artist-dialog/update-artist-dialog.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  private artistsMap = new Map<string, Artist[]>();

  listIsLoaded = false;
  currentUser = '';

  constructor(
    private router: Router,
    private tokenRefreshService: RefreshService,
    private allArtistsService: GetAllArtistsService,
    private localStorage: MyStorageService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    if (this.doesntHaveJwtToken()) {
      this.redirectToLoginPage();
    }
    this.loadListsOfArtists();
  }

  openAddDialog = () => {
    const dialogAddRef = this.dialog.open(AddArtistDialogComponent);

    dialogAddRef.afterClosed().subscribe((result) => {
      if (!result) {
        this.snackBar.open('Falha ao adicionar o artista.', 'Ok');
        return;
      }

      this.snackBar.open('O artista foi adicionado com sucesso!', '', {
        duration: 3000
      });
      this.loadListsOfArtists(false);
    });
  }

  openUpdateDialog = (artist: Artist) => {
    const dialogUpdateRef = this.dialog.open(UpdateArtistDialogComponent, {
      data: {
        artist,
      },
    });
    dialogUpdateRef.afterClosed().subscribe((result) => {
      if (!result) {
        this.snackBar.open('Falha ao editar o artista.', 'Ok');
        return;
      }

      this.snackBar.open('O artista foi editado com sucesso!', '', {
        duration: 3000
      });
      this.loadListsOfArtists(false);
    });
  }

  openDeleteDialog = (artist: Artist) => {
    const dialogDeleteRef = this.dialog.open(DeleteArtistDialogComponent, {
      data: {
        artist,
      },
    });
    dialogDeleteRef.afterClosed().subscribe((result) => {
      if (!result) {
        this.snackBar.open('Falha ao remover o artista.', 'Ok');
        return;
      }

      this.snackBar.open('O artista foi removido com sucesso!', '', {
        duration: 3000
      });
      this.loadListsOfArtists(false);
    });
  }

  doLogout(): void {
    this.localStorage.removeUsername();
    this.localStorage.removeJwtToken();

    this.router.navigateByUrl('/login').then();
  }

  getMapEntries = (): [string, Artist[]][] => {
    return Array.from(this.artistsMap.entries());
  }

  private doesntHaveJwtToken = (): boolean => {
    return !this.localStorage.haveJwtToken();
  }

  private redirectToLoginPage = (): void => {
    this.router.navigateByUrl('/login').then();
  }

  private loadListsOfArtists = (shouldRetry = true) => {
    const successWhenLoadingTheList = (pagedList: PagedArtistResponse) => {
      this.artistsMap.clear();
      pagedList.artists.forEach(this.createMapOfArtists);

      this.listIsLoaded = true;
      this.currentUser = this.localStorage.getUsername();
    };

    const errorWhenLoadingTheList = (e: HttpErrorResponse) => {
      if (!shouldRetry || [401, 403].indexOf(e.status) === -1) {
        this.snackBar.open(
          'O servidor está fora do ar no momento! Por favor, tente novamente mais tarde!',
          'Ok'
        );
        return;
      }

      this.refreshTheTokenAndTryAgain();
    };

    this.allArtistsService
      .requestArtistsList(0, 1000000, 'asc')
      .subscribe(successWhenLoadingTheList, errorWhenLoadingTheList);
  }

  private refreshTheTokenAndTryAgain = (): void => {
    const refreshSuccess = () => this.loadListsOfArtists(false);
    const refreshFailed = () => {
      this.localStorage.removeUsername();
      this.redirectToLoginPage();
    };

    this.tokenRefreshService.refreshJwtTokenIfExpired(
      refreshSuccess,
      refreshFailed
    );
  }

  private createMapOfArtists = (artist: Artist): void => {
    if (artist.name.length <= 0) {
      return;
    }

    const key = artist.name.charAt(0).toLowerCase();
    let theArtists = this.artistsMap.get(key);

    if (!theArtists) {
      theArtists = [];
    }

    this.artistsMap.set(key, [...theArtists, artist]);
  }
}
