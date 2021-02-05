import { TestBed } from '@angular/core/testing';

import { AddArtistService } from './add-artist.service';

describe('AddArtistService', () => {
  let service: AddArtistService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AddArtistService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
