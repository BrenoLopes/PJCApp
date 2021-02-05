import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialDesignModule } from '@matdesign/materialdesign.module';

import { CoreModule } from '@core/core.module';

import { AddArtistDialogComponent } from './dialog/artist/add-artist-dialog/add-artist-dialog.component';
import { UpdateArtistDialogComponent } from './dialog/artist/update-artist-dialog/update-artist-dialog.component';
import { DeleteArtistDialogComponent } from './dialog/artist/delete-artist-dialog/delete-artist-dialog.component';

@NgModule({
  declarations: [
    AddArtistDialogComponent,
    UpdateArtistDialogComponent,
    DeleteArtistDialogComponent,
  ],
  imports: [CommonModule, MaterialDesignModule, CoreModule],
  entryComponents: [
    AddArtistDialogComponent,
    UpdateArtistDialogComponent,
    DeleteArtistDialogComponent,
  ],
})
export class SharedModule {}
