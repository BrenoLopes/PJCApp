import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Artist } from '@core/services/api/artists';
import { RemoveArtistService } from '@core/services/api/artists/remove-artist.service';
import { AddArtistResponse } from '@core/services/api/artists/add-artist.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-delete-artist-dialog',
  templateUrl: './delete-artist-dialog.component.html',
  styleUrls: ['./delete-artist-dialog.component.scss'],
})
export class DeleteArtistDialogComponent implements OnInit {
  constructor(
    public dialogRef: MatDialogRef<DeleteArtistDialogComponent>,
    private deleteArtist: RemoveArtistService,
    @Inject(MAT_DIALOG_DATA) public data: { artist: Artist }
  ) {}

  ngOnInit(): void {}

  onDelete(): void {
    const success = (r: AddArtistResponse) => {
      this.dialogRef.close(true);
    };

    const error = (e: HttpErrorResponse) => {
      this.dialogRef.close(false);
    };

    this.deleteArtist
      .requestRemoval(this.data.artist.id)
      .subscribe(success, error);
  }
}
