import { TestBed } from '@angular/core/testing';

import { RemoveAlbumService } from './remove-album.service';

describe('RemoveAlbumService', () => {
  let service: RemoveAlbumService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RemoveAlbumService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
