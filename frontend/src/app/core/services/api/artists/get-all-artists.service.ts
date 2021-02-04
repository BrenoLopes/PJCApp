import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '@env';

import { PagedArtistResponse } from '@core/services/api/artists/index';
import { MyStorageService } from '@core/services/storage/my-storage.service';

@Injectable()
export class GetAllArtistsService {
  private serverUrl = environment.serverUrl;

  constructor(
    private http: HttpClient,
    private localStorage: MyStorageService
  ) {}

  requestArtistsList(
    page: number,
    pageSize: number,
    direction: string
  ): Observable<PagedArtistResponse> {
    const url = `${this.serverUrl}/api/artists/list`;
    const token = this.localStorage.getJwtToken();

    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`)
      .set('Content-Type', 'application/json; charset=utf-8')
      .set('Accept', 'application/json');

    const params = new HttpParams();
    params.set('page', isNaN(page) ? '0' : page.toString());
    params.set('direction', direction);
    params.set('pagesize', isNaN(pageSize) ? '10' : pageSize.toString());

    return this.http.get<PagedArtistResponse>(url, { headers, params });
  }
}
