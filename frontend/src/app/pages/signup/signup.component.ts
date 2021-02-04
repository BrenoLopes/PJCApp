import { Component, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';

import {
  SignUpRequest,
  SignUpService,
} from '@core/services/auth/signup/sign-up.service';
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
      const message: string = this.createMessageFromError(error);

      this.snackBar.open(message, 'Ok');
      runAfter();
    };

    this.signUpService
      .requestSignUp(request)
      .subscribe(onSuccessfulSignUp, onFailedSignUp);
  }

  private createMessageFromError(error: HttpErrorResponse): string {
    if (error.status === 401) {
      return 'Suas credenciais estão incorretas!';
    } else if (error.status === 409) {
      return 'Whoops, este email já está cadastrado!';
    }

    return 'Whoops, algo de errado aconteceu no servidor!';
  }
}
