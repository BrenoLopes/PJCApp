import { TestBed } from '@angular/core/testing';

import { GetAllAlbumsService } from './get-all-albums.service';

describe('GetAllAlbumsService', () => {
  let service: GetAllAlbumsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GetAllAlbumsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
