import { Injectable } from '@angular/core';

@Injectable()
export class MyStorageService {
  private tokenKey = this.getTokenKey();
  private usernameKey = this.getUsernameKey();

  constructor() {}

  haveJwtToken(): boolean {
    return localStorage.getItem(this.tokenKey) != null;
  }

  haveUsername(): boolean {
    return localStorage.getItem(this.usernameKey) != null;
  }

  getJwtToken(): string {
    const result = localStorage.getItem(this.tokenKey);

    if (result === null) {
      return '';
    }

    return this.binaryToString(atob(result));
  }

  getUsername(): string {
    const result = localStorage.getItem(this.usernameKey);

    if (result === null) {
      return '';
    }

    return this.binaryToString(atob(result));
  }

  setJwtToken(token: string): void {
    const theToken = btoa(this.stringToBinary(token));
    localStorage.setItem(this.tokenKey, theToken);
  }

  setUsername(username: string): void {
    const theToken = btoa(this.stringToBinary(username));
    localStorage.setItem(this.usernameKey, theToken);
  }

  removeJwtToken(): void {
    localStorage.removeItem(this.tokenKey);
  }

  removeUsername(): void {
    localStorage.removeItem(this.usernameKey);
  }

  private getTokenKey(): string {
    return btoa(this.stringToBinary('da_token'));
  }

  private getUsernameKey(): string {
    return btoa(this.stringToBinary('da_username_token'));
  }

  private stringToBinary(text: string): string {
    return text
      .split('')
      .map((char) => {
        return char.charCodeAt(0).toString(2);
      })
      .join(' ');
  }

  private binaryToString(binary: string): string {
    return binary
      .split(' ')
      .map((bin) => String.fromCharCode(parseInt(bin, 2)))
      .join('');
  }
}
