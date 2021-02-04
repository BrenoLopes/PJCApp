import { NgModule } from '@angular/core';
import { StoreModule } from '@ngrx/store';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';

import { reducer } from '@core/appstate/reducers';

import { SignUpService } from '@core/services/auth/signup/sign-up.service';
import { MyStorageService } from '@core/services/storage/my-storage.service';
import { LoginService } from '@core/services/auth/login/login.service';

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
  providers: [SignUpService, MyStorageService, LoginService],
})
export class CoreModule {}
