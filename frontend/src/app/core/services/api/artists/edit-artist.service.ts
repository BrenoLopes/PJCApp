import { Injectable } from '@angular/core';
import { environment } from '@env';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { MyStorageService } from '@core/services/storage/my-storage.service';

@Injectable()
export class EditArtistService {
  private serverUrl = environment.serverUrl;

  constructor(
    private http: HttpClient,
    private localStorage: MyStorageService
  ) {}

  requestUpdate(request: EditArtistRequest): Observable<EditArtistResponse> {
    const url = `${this.serverUrl}/api/artists`;
    const token = this.localStorage.getJwtToken();

    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`)
      .set('Content-Type', 'application/json; charset=utf-8')
      .set('Accept', 'application/json');

    return this.http.put<EditArtistResponse>(url, request, { headers });
  }
}

export interface EditArtistRequest {
  readonly id: number;
  readonly name: string;
}

export interface EditArtistResponse {
  readonly message: string;
}
