import { TestBed } from '@angular/core/testing';

import { RemoveArtistService } from './remove-artist.service';

describe('RemoveArtistService', () => {
  let service: RemoveArtistService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RemoveArtistService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
