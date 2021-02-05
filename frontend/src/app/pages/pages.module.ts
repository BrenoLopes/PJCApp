import { NgModule } from '@angular/core';

import { MaterialDesignModule } from '@matdesign/materialdesign.module';
import { CoreModule } from '@core/core.module';

import { HomeComponent } from '@pages/home/home.component';
import { LoginComponent } from '@pages/login/login.component';
import { SignupComponent } from '@pages/signup/signup.component';
import { NotfoundComponent } from '@pages/notfound/notfound.component';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  declarations: [
    HomeComponent,
    LoginComponent,
    SignupComponent,
    NotfoundComponent,
  ],
  imports: [CoreModule, MaterialDesignModule, SharedModule],
})
export class PagesModule {}
