import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteArtistDialogComponent } from './delete-artist-dialog.component';

describe('DeleteArtistDialogComponent', () => {
  let component: DeleteArtistDialogComponent;
  let fixture: ComponentFixture<DeleteArtistDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeleteArtistDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteArtistDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
