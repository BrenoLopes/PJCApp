import { TestBed } from '@angular/core/testing';

import { GetSingleArtistService } from './get-single-artist.service';

describe('GetSingleArtistService', () => {
  let service: GetSingleArtistService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GetSingleArtistService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
