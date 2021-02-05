import { TestBed } from '@angular/core/testing';

import { EditAlbumService } from './edit-album.service';

describe('EditAlbumService', () => {
  let service: EditAlbumService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EditAlbumService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
