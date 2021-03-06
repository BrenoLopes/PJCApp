import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

import { Artist } from '@core/services/api/artists';
import {
  EditArtistResponse,
  EditArtistService,
} from '@core/services/api/artists/edit-artist.service';

@Component({
  selector: 'app-update-artist-dialog',
  templateUrl: './update-artist-dialog.component.html',
  styleUrls: ['./update-artist-dialog.component.scss'],
})
export class UpdateArtistDialogComponent implements OnInit {
  editForm!: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<UpdateArtistDialogComponent>,
    private formBuilder: FormBuilder,
    private editArtistService: EditArtistService,
    @Inject(MAT_DIALOG_DATA) public data: { artist: Artist }
  ) {}

  ngOnInit(): void {
    this.editForm = this.formBuilder.group({
      name: ['', [Validators.required]],
    });
  }

  getName(): AbstractControl | null {
    return this.editForm.get('name');
  }

  onUpdate = (cancelled = false): void => {
    if (cancelled) {
      this.dialogRef.close(EditReturnType.CANCELED);
      return;
    }

    const success = (r: EditArtistResponse) => {
      this.dialogRef.close(EditReturnType.ADDED);
    };

    const error = (e: HttpErrorResponse) => {
      if (e.status === 409) {
        this.dialogRef.close(EditReturnType.CONFLICT);
      }

      this.dialogRef.close(EditReturnType.FAILED);
    };

    this.editArtistService
      .requestUpdate({ id: this.data.artist.id, name: this.getName()?.value })
      .subscribe(success, error);
  }
}

export enum EditReturnType {
  CONFLICT,
  CANCELED,
  FAILED,
  ADDED,
}
