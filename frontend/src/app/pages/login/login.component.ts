import { Component, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';

import {
  LoginRequest,
  LoginResponse,
  LoginService,
} from '@core/services/auth/login/login.service';
import { MyStorageService } from '@core/services/storage/my-storage.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private loginService: LoginService,
    private snackBar: MatSnackBar,
    private localStorage: MyStorageService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (this.localStorage.haveJwtToken()) {
      this.navigateToHome();
    }

    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
    });
  }

  getEmail(): AbstractControl | null {
    return this.loginForm.get('email');
  }

  getPassword(): AbstractControl | null {
    return this.loginForm.get('password');
  }

  onSubmit(): void {
    this.loginForm.disable();

    const runWhenRequestEnds = () => this.loginForm.enable();
    this.doTheRequest(runWhenRequestEnds);
  }

  private navigateToHome(): void {
    this.router.navigateByUrl('/').then();
  }

  private doTheRequest(runAfter: () => void): void {
    const request: LoginRequest = {
      username: this.getEmail()?.value,
      password: this.getPassword()?.value,
    };

    const onSuccessfulSignUp = (response: LoginResponse) => {
      this.snackBar.open('O login foi feito com sucesso!');

      this.localStorage.setJwtToken(response.token);
      this.localStorage.setUsername(response.username);

      this.navigateToHome();
    };

    const onFailedSignUp = (error: HttpErrorResponse) => {
      let message: string;

      if (error.status === 401) {
        message = 'Suas credenciais estão incorretas!';
      } else {
        message = 'Whoops, algo de errado aconteceu no servidor!';
      }

      this.snackBar.open(message, 'Ok');
      runAfter();
    };

    this.loginService
      .requestLogin(request)
      .subscribe(onSuccessfulSignUp, onFailedSignUp);
  }
}
