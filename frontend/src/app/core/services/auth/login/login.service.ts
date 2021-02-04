import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from 'rxjs';

import { environment } from '@env';

@Injectable()
export class LoginService {
  private serverUrl = environment.serverUrl;

  constructor(private http: HttpClient) {}

  requestLogin(request: LoginRequest): Observable<LoginResponse> {
    const url = `${this.serverUrl}/api/auth/login`;
    return this.http.post<LoginResponse>(url, request);
  }
}

export interface LoginRequest {
  readonly username: string;
  readonly password: string;
}

export interface LoginResponse {
  readonly username: string;
  readonly token: string;
}
