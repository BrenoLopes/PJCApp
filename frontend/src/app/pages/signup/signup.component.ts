import { Component, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';

import {
  SignUpRequest,
  SignUpResponse,
  SignUpService,
} from '@core/services/auth/signup/sign-up.service';
import { ErrorObserver, NextObserver } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss'],
})
export class SignupComponent implements OnInit {
  signUpForm!: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private signUpService: SignUpService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.signUpForm = this.formBuilder.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
    });
  }

  onSubmit(): void {
    this.signUpForm.disable();

    const request: SignUpRequest = {
      name: this.getName()?.value,
      password: this.getPassword()?.value,
      username: this.getEmail()?.value,
    };

    const onSuccessfulSignUp = () => {
      this.snackBar.open('Cadastro feito com sucesso!');
      this.signUpForm.enable();
    };

    const onFailedSignUp = (error: HttpErrorResponse) => {
      let message = '';

      if (error.status === 401) {
        message = 'Suas credenciais estão incorretas!';
      } else if (error.status === 409) {
        message = 'Whoops, este email já está cadastrado!';
      } else {
        message = 'Whoops, algo de errado aconteceu no servidor!';
      }

      this.snackBar.open(message, 'Ok');
      this.signUpForm.enable();
    };

    this.signUpService
      .requestSignUp(request)
      .subscribe(onSuccessfulSignUp, onFailedSignUp);
  }

  getName(): AbstractControl | null {
    return this.signUpForm.get('name');
  }

  getEmail(): AbstractControl | null {
    return this.signUpForm.get('email');
  }

  getPassword(): AbstractControl | null {
    return this.signUpForm.get('password');
  }
}
