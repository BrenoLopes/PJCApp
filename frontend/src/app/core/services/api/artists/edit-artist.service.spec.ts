import { TestBed } from '@angular/core/testing';

import { EditArtistService } from './edit-artist.service';

describe('EditArtistService', () => {
  let service: EditArtistService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EditArtistService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
