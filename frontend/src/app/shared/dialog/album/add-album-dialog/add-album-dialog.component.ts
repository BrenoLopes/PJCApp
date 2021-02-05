import {Component, Inject, OnInit} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {
  AddArtistResponse,
} from '@core/services/api/artists/add-artist.service';
import { HttpErrorResponse } from '@angular/common/http';
import { AddAlbumService } from '@core/services/api/albums/add-album.service';
import {Artist} from '@core/services/api/artists';

@Component({
  selector: 'app-add-album-dialog',
  templateUrl: './add-album-dialog.component.html',
  styleUrls: ['./add-album-dialog.component.scss'],
})
export class AddAlbumDialogComponent implements OnInit {
  addForm!: FormGroup;

  constructor(
    private dialogRef: MatDialogRef<AddAlbumDialogComponent>,
    private formBuilder: FormBuilder,
    private addAlbumService: AddAlbumService,
    @Inject(MAT_DIALOG_DATA) public data: { artist: Artist }
  ) {}

  ngOnInit(): void {
    this.addForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      image: ['', [Validators.required]],
    });
  }

  getName(): AbstractControl | null {
    return this.addForm.get('name');
  }

  getImage(): AbstractControl | null {
    return this.addForm.get('image');
  }

  onAddAlbum = (cancelled = false): void => {
    if (cancelled) {
      this.dialogRef.close(AddReturnTypes.CANCELED);
      return;
    }

    const success = (r: AddArtistResponse) => {
      this.dialogRef.close(AddReturnTypes.ADDED);
    };

    const error = (e: HttpErrorResponse) => {
      if (e.status === 409) {
        this.dialogRef.close(AddReturnTypes.CONFLICT);
      }

      this.dialogRef.close(AddReturnTypes.FAILED);
    };

    const albumName = this.getName()?.value;
    const albumImage = this.getImage()?.value as File;

    this.addAlbumService
      .requestAdd(albumName, albumImage, this.data.artist.id)
      .subscribe(success, error);
  }

  onFileSelect(event: any): void {
    if (!event || !event.target ) {
      return;
    }

    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.getImage()?.setValue(file);
    }
  }
}

export enum AddReturnTypes {
  CONFLICT,
  CANCELED,
  FAILED,
  ADDED
}
