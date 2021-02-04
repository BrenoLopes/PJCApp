import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '@env';

@Injectable({
  providedIn: 'root',
})
export class SignUpService {
  private serverUrl = environment.serverUrl;

  constructor(private http: HttpClient) {}

  requestSignUp(request: SignUpRequest): Observable<SignUpResponse> {
    const url = `${this.serverUrl}/api/auth/signup`;
    return this.http.post<SignUpResponse>(url, request);
  }
}

export interface SignUpRequest {
  username: string;
  name: string;
  password: string;
}

export interface SignUpResponse {
  readonly message: string;
}
