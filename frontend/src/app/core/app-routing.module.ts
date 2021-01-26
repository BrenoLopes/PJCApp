import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import {HomeComponent} from '@pages/home/home.component';
import {LoginComponent} from '@pages/login/login.component';
import {SignupComponent} from '@pages/signup/signup.component';
import {NotfoundComponent} from '@pages/notfound/notfound.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'cadastrar', component: SignupComponent },
  { path: 'nao_encontrado', component: NotfoundComponent },
  { path: '**', redirectTo: 'nao_encontrado' }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes),
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
