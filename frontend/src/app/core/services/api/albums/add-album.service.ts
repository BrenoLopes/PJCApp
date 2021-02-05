import { Injectable } from '@angular/core';
import { environment } from '@env';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MyStorageService } from '@core/services/storage/my-storage.service';
import { Observable } from 'rxjs';

@Injectable()
export class AddAlbumService {
  private serverUrl = environment.serverUrl;

  constructor(
    private http: HttpClient,
    private localStorage: MyStorageService
  ) {}

  requestAdd(
    name: string,
    image: any,
    artistid: number
  ): Observable<AddAlbumResponse> {
    const url = `${this.serverUrl}/api/albums`;
    const token = this.localStorage.getJwtToken();

    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`);

    const request = new FormData();
    request.set('name', name);
    request.set('artist_id', artistid.toString());

    if (image && image !== '') {
      request.set('image', image);
    }

    return this.http.post<AddAlbumResponse>(url, request, { headers });
  }
}

export interface AddAlbumResponse {
  readonly message: string;
}
