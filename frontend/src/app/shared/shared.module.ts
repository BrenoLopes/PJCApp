import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialDesignModule } from '@matdesign/materialdesign.module';

import { CoreModule } from '@core/core.module';

import { AddArtistDialogComponent } from './dialog/artist/add-artist-dialog/add-artist-dialog.component';
import { UpdateArtistDialogComponent } from './dialog/artist/update-artist-dialog/update-artist-dialog.component';
import { DeleteArtistDialogComponent } from './dialog/artist/delete-artist-dialog/delete-artist-dialog.component';
import { AddAlbumDialogComponent } from './dialog/album/add-album-dialog/add-album-dialog.component';
import { EditAlbumDialogComponent } from './dialog/album/edit-album-dialog/edit-album-dialog.component';
import { DeleteAlbumDialogComponent } from './dialog/album/delete-album-dialog/delete-album-dialog.component';

@NgModule({
  declarations: [
    AddArtistDialogComponent,
    UpdateArtistDialogComponent,
    DeleteArtistDialogComponent,
    AddAlbumDialogComponent,
    EditAlbumDialogComponent,
    DeleteAlbumDialogComponent,
  ],
  imports: [CommonModule, MaterialDesignModule, CoreModule],
  entryComponents: [
    AddArtistDialogComponent,
    UpdateArtistDialogComponent,
    DeleteArtistDialogComponent,
  ],
})
export class SharedModule {}
