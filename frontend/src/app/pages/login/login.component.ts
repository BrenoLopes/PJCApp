import { Component, OnInit } from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;

  constructor(
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
    });
  }

  onSubmit(): void {
    this.loginForm.disable();

    console.log(`You have tried to log in with ${this.getEmail()?.value} and ${this.getPassword()?.value}`);

    this.loginForm.enable();
  }

  getEmail(): AbstractControl | null {
    return this.loginForm.get('email');
  }

  getPassword(): AbstractControl | null {
    return this.loginForm.get('password');
  }
}
