import { TestBed } from '@angular/core/testing';

import { AddAlbumService } from './add-album.service';

describe('AddAlbumService', () => {
  let service: AddAlbumService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AddAlbumService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
