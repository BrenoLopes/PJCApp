import { Component, OnInit } from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit {
  signUpForm!: FormGroup;

  constructor(
    private formBuilder: FormBuilder
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

    console.log(`You have tried to log in with ${this.getEmail()?.value} and ${this.getPassword()?.value}`);

    this.signUpForm.enable();
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
