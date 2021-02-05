import { Component, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpErrorResponse } from '@angular/common/http';

import {
  SignUpRequest,
  SignUpService,
} from '@core/services/auth/signup/sign-up.service';
import { MyStorageService } from '@core/services/storage/my-storage.service';

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
    private snackBar: MatSnackBar,
    private localStorage: MyStorageService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (this.doesTheUserHaveToken()) {
      this.navigateToHome();
    }

    this.signUpForm = this.formBuilder.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
    });
  }

  onSubmit(): void {
    this.signUpForm.disable();

    const runWhenRequestEnds = () => this.signUpForm.enable();

    this.doTheRequest(runWhenRequestEnds);
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

  private doesTheUserHaveToken(): boolean {
    return this.localStorage.getJwtToken() !== '';
  }

  private navigateToHome(): void {
    this.router.navigateByUrl('/').then();
  }

  private doTheRequest(runAfter: () => void): void {
    const request: SignUpRequest = {
      name: this.getName()?.value,
      password: this.getPassword()?.value,
      username: this.getEmail()?.value,
    };

    const onSuccessfulSignUp = () => {
      this.snackBar.open('Cadastro feito com sucesso!');
      runAfter();
    };

    const onFailedSignUp = (error: HttpErrorResponse) => {
      let message: string;

      if (error.status === 401) {
        message = 'Suas credenciais estão incorretas!';
      } else if (error.status === 409) {
        message = 'Whoops, este email já está cadastrado!';
      } else {
        message = 'Whoops, algo de errado aconteceu no servidor!';
      }

      this.snackBar.open(message, 'Ok');
      runAfter();
    };

    this.signUpService
      .requestSignUp(request)
      .subscribe(onSuccessfulSignUp, onFailedSignUp);
  }
}
