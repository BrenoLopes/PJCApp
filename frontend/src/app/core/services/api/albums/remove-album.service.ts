import { Injectable } from '@angular/core';
import { environment } from '@env';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { MyStorageService } from '@core/services/storage/my-storage.service';
import { Observable } from 'rxjs';

@Injectable()
export class RemoveAlbumService {
  private serverUrl = environment.serverUrl;

  constructor(
    private http: HttpClient,
    private localStorage: MyStorageService
  ) {}

  requestRemoval(id: number): Observable<RemoveAlbumResponse> {
    const url = `${this.serverUrl}/api/albums`;
    const token = this.localStorage.getJwtToken();

    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`)
      .set('Content-Type', 'application/json; charset=utf-8')
      .set('Accept', 'application/json');

    const params = new HttpParams().set('id', id.toString());

    return this.http.delete<RemoveAlbumResponse>(url, { headers, params });
  }
}

export interface RemoveAlbumResponse {
  readonly message: string;
}
