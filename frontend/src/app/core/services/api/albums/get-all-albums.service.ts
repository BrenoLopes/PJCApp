import { Injectable } from '@angular/core';
import { environment } from '@env';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import { MyStorageService } from '@core/services/storage/my-storage.service';
import {Observable} from 'rxjs';
import {Artist, PagedArtistResponse} from '@core/services/api/artists';
import {Album} from '@core/services/api/albums/index';

@Injectable()
export class GetAllAlbumsService {
  private serverUrl = environment.serverUrl;

  constructor(
    private http: HttpClient,
    private localStorage: MyStorageService
  ) {}

  requestAlbumsList(
    artistId: number,
    page: number,
    pageSize: number,
    direction: string
  ): Observable<PagedAlbumResponse> {
    const url = `${this.serverUrl}/api/albums/list`;
    const token = this.localStorage.getJwtToken();

    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`)
      .set('Content-Type', 'application/json; charset=utf-8')
      .set('Accept', 'application/json');

    const params = new HttpParams()
      .set('page', isNaN(page) ? '0' : page.toString())
      .set('direction', direction)
      .set('pagesize', isNaN(pageSize) ? '10' : pageSize.toString())
      .set('artistid', artistId.toString());

    return this.http.get<PagedAlbumResponse>(url, { headers, params });
  }
}

export interface PagedAlbumResponse {
  readonly albums: Array<Album>;
  readonly currentPage: number;
  readonly totalPages: number;
  readonly totalItems: number;
}
