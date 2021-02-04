import { Component, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpErrorResponse } from '@angular/common/http';

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
    private localStorage: MyStorageService
  ) {}

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
    });
  }

  onSubmit(): void {
    this.loginForm.disable();

    const runWhenRequestEnds = () => this.loginForm.enable();
    this.doTheRequest(runWhenRequestEnds);
  }

  getEmail(): AbstractControl | null {
    return this.loginForm.get('email');
  }

  getPassword(): AbstractControl | null {
    return this.loginForm.get('password');
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

      console.log(this.localStorage.getUsername(), this.localStorage.getJwtToken());

      runAfter();
    };

    const onFailedSignUp = (error: HttpErrorResponse) => {
      const message: string = this.createMessageFromError(error);

      this.snackBar.open(message, 'Ok');
      runAfter();
    };

    this.loginService
      .requestLogin(request)
      .subscribe(onSuccessfulSignUp, onFailedSignUp);
  }

  private createMessageFromError(error: HttpErrorResponse): string {
    if (error.status === 401) {
      return 'Suas credenciais estão incorretas!';
    }

    return 'Whoops, algo de errado aconteceu no servidor!';
  }
}
