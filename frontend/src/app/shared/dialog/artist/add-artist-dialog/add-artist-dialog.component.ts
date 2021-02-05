import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';

import {
  AddArtistResponse,
  AddArtistService,
} from '@core/services/api/artists/add-artist.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-add-artist-dialog',
  templateUrl: './add-artist-dialog.component.html',
  styleUrls: ['./add-artist-dialog.component.scss'],
})
export class AddArtistDialogComponent implements OnInit {
  addForm!: FormGroup;

  constructor(
    private dialogRef: MatDialogRef<AddArtistDialogComponent>,
    private formBuilder: FormBuilder,
    private addArtistService: AddArtistService
  ) {}

  ngOnInit(): void {
    this.addForm = this.formBuilder.group({
      name: ['', [Validators.required]],
    });
  }

  getName(): AbstractControl | null {
    return this.addForm.get('name');
  }

  onAddArtist = (): void => {
    const success = (r: AddArtistResponse) => {
      this.dialogRef.close(true);
    };

    const error = (e: HttpErrorResponse) => {
      this.dialogRef.close(false);
    };

    this.addArtistService
      .requestAdd({ name: this.getName()?.value })
      .subscribe(success, error);
  }
}
