import { Albums } from '@core/services/api/albums';

export interface Artist {
  readonly id: number;
  readonly name: string;
  readonly albums: Albums;
  readonly created_by: string;
}

export interface PagedArtistResponse {
  readonly artists: Array<Artist>;
  readonly currentPage: number;
  readonly totalPages: number;
  readonly totalItems: number;
}
