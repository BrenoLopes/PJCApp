import { NgModule } from '@angular/core';
import { StoreModule } from '@ngrx/store';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';

import { reducer } from '@core/appstate/reducers';

import { SignUpService } from '@core/services/auth/signup/sign-up.service';
import { MyStorageService } from '@core/services/storage/my-storage.service';
import { LoginService } from '@core/services/auth/login/login.service';
import { RefreshService } from '@core/services/auth/refresh/refresh.service';
import { GetAllArtistsService } from '@core/services/api/artists/get-all-artists.service';
import { AddArtistService } from '@core/services/api/artists/add-artist.service';
import { EditArtistService } from '@core/services/api/artists/edit-artist.service';
import { RemoveArtistService } from '@core/services/api/artists/remove-artist.service';

import { AppRoutingModule } from './app-routing.module';

@NgModule({
  declarations: [],
  imports: [
    AppRoutingModule,
    BrowserModule,
    BrowserAnimationsModule,
    StoreModule.forRoot({
      appContext: reducer,
    }),
  ],
  exports: [
    AppRoutingModule,
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
  ],
  providers: [
    SignUpService,
    MyStorageService,
    LoginService,
    RefreshService,
    GetAllArtistsService,
    AddArtistService,
    EditArtistService,
    RemoveArtistService,
  ],
})
export class CoreModule {}
