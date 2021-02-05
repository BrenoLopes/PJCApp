import { Injectable } from '@angular/core';
import { environment } from '@env';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { MyStorageService } from '@core/services/storage/my-storage.service';
import { Artist } from '@core/services/api/artists/index';
import { Observable } from 'rxjs';

@Injectable()
export class GetSingleArtistService {
  private serverUrl = environment.serverUrl;

  constructor(
    private http: HttpClient,
    private localStorage: MyStorageService
  ) {}

  requestArtist(name: string): Observable<Artist> {
    const url = `${this.serverUrl}/api/artists`;
    const token = this.localStorage.getJwtToken();

    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`)
      .set('Content-Type', 'application/json; charset=utf-8')
      .set('Accept', 'application/json');

    const params = new HttpParams().set('name', name);

    return this.http.get<Artist>(url, { headers, params });
  }
}
