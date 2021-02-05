import { Injectable } from '@angular/core';
import { environment } from '@env';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MyStorageService } from '@core/services/storage/my-storage.service';
import { Observable } from 'rxjs';
import { AddAlbumResponse } from '@core/services/api/albums/add-album.service';

@Injectable({
  providedIn: 'root',
})
export class EditAlbumService {
  private serverUrl = environment.serverUrl;

  constructor(
    private http: HttpClient,
    private localStorage: MyStorageService
  ) {}

  requestEdit(
    id: number,
    image: any,
    name: string
  ): Observable<AddAlbumResponse> {
    const url = `${this.serverUrl}/api/albums`;
    const token = this.localStorage.getJwtToken();

    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`);

    const request = new FormData();
    request.set('id', id.toString());
    request.set('name', name.toString());

    if (image && image !== '') {
      request.set('image', image);
    }

    return this.http.put<AddAlbumResponse>(url, request, { headers });
  }
}
