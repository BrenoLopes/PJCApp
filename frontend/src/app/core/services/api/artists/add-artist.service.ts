import { Injectable } from '@angular/core';
import { environment } from '@env';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MyStorageService } from '@core/services/storage/my-storage.service';
import { Observable } from 'rxjs';

@Injectable()
export class AddArtistService {
  private serverUrl = environment.serverUrl;

  constructor(
    private http: HttpClient,
    private localStorage: MyStorageService
  ) {}

  requestAdd(request: AddArtistRequest): Observable<AddArtistResponse> {
    const url = `${this.serverUrl}/api/artists`;
    const token = this.localStorage.getJwtToken();

    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`)
      .set('Content-Type', 'application/json; charset=utf-8')
      .set('Accept', 'application/json');

    return this.http.post<AddArtistResponse>(url, request, { headers });
  }
}

export interface AddArtistRequest {
  readonly name: string;
}

export interface AddArtistResponse {
  readonly message: string;
}
