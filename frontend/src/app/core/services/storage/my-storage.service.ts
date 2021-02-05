import { Injectable } from '@angular/core';

@Injectable()
export class MyStorageService {
  private tokenKey = 'da_token';
  private usernameKey = 'da_username_token';

  constructor() {}

  haveJwtToken(): boolean {
    return this.getJwtToken() !== '';
  }

  haveUsername(): boolean {
    return this.getUsername() != null;
  }

  getJwtToken(): string {
    const token = localStorage.getItem(this.tokenKey);
    return token === null ? '' : token;
  }

  getUsername(): string {
    const username = localStorage.getItem(this.usernameKey);
    return username === null ? '' : username;
  }

  setJwtToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  setUsername(username: string): void {
    localStorage.setItem(this.usernameKey, username);
  }

  removeJwtToken(): void {
    localStorage.removeItem(this.tokenKey);
  }

  removeUsername(): void {
    localStorage.removeItem(this.usernameKey);
  }
}
