import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder } from '@angular/forms';
import { RemoveAlbumService } from '@core/services/api/albums/remove-album.service';
import { AddArtistResponse } from '@core/services/api/artists/add-artist.service';
import { HttpErrorResponse } from '@angular/common/http';
import { AddReturnTypes } from '@shared/dialog/album/add-album-dialog/add-album-dialog.component';
import { Album } from '@core/services/api/albums';

@Component({
  selector: 'app-remove-album-dialog',
  templateUrl: './delete-album-dialog.component.html',
  styleUrls: ['./delete-album-dialog.component.scss'],
})
export class DeleteAlbumDialogComponent implements OnInit {
  constructor(
    private dialogRef: MatDialogRef<DeleteAlbumDialogComponent>,
    private formBuilder: FormBuilder,
    private removeAlbumService: RemoveAlbumService,
    @Inject(MAT_DIALOG_DATA) public data: { album: Album }
  ) {}

  ngOnInit(): void {}

  onRemoveAlbum = (cancelled = false) => {
    if (cancelled) {
      this.dialogRef.close(RemoveReturnTypes.CANCELED);
      return;
    }

    const success = (r: AddArtistResponse) => {
      this.dialogRef.close(RemoveReturnTypes.REMOVED);
    };

    const error = (e: HttpErrorResponse) => {
      this.dialogRef.close(AddReturnTypes.FAILED);
    };

    this.removeAlbumService
      .requestRemoval(this.data.album.id)
      .subscribe(success, error);
  }
}

export enum RemoveReturnTypes {
  CANCELED,
  FAILED,
  REMOVED,
}
