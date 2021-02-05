import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Artist } from '@core/services/api/artists';
import { EditAlbumService } from '@core/services/api/albums/edit-album.service';
import { AddReturnTypes } from '@shared/dialog/album/add-album-dialog/add-album-dialog.component';
import { AddArtistResponse } from '@core/services/api/artists/add-artist.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Album } from '@core/services/api/albums';

@Component({
  selector: 'app-edit-album-dialog',
  templateUrl: './edit-album-dialog.component.html',
  styleUrls: ['./edit-album-dialog.component.scss'],
})
export class EditAlbumDialogComponent implements OnInit {
  editForm!: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<EditAlbumDialogComponent>,
    private formBuilder: FormBuilder,
    private editAlbumService: EditAlbumService,
    @Inject(MAT_DIALOG_DATA) public data: { album: Album }
  ) {}

  ngOnInit(): void {
    this.editForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      image: [''],
    });
  }

  getName(): AbstractControl | null {
    return this.editForm.get('name');
  }

  getImage(): AbstractControl | null {
    return this.editForm.get('image');
  }

  onFileSelect(event: any): void {
    if (!event || !event.target) {
      return;
    }

    if (event.target.files.length > 0) {
      const file = event.target.files[0];

      this.editForm.get('image')?.setValue(file);
    }
  }

  onEditAlbum = (cancelled = false): void => {
    if (cancelled) {
      this.dialogRef.close(EditReturnType.CANCELED);
      return;
    }

    const success = (r: AddArtistResponse) => {
      this.dialogRef.close(EditReturnType.ADDED);
    };

    const error = (e: HttpErrorResponse) => {
      if (e.status === 409) {
        this.dialogRef.close(EditReturnType.CONFLICT);
      }

      this.dialogRef.close(EditReturnType.FAILED);
    };

    const albumName = this.getName()?.value;
    const albumImage = this.getImage()?.value;

    this.editAlbumService
      .requestEdit(this.data.album.id, albumImage, albumName)
      .subscribe(success, error);
  }
}

export enum EditReturnType {
  CONFLICT,
  CANCELED,
  FAILED,
  ADDED,
}
