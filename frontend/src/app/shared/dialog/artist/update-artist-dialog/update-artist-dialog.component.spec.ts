import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateArtistDialogComponent } from './update-artist-dialog.component';

describe('UpdateArtistDialogComponent', () => {
  let component: UpdateArtistDialogComponent;
  let fixture: ComponentFixture<UpdateArtistDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateArtistDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateArtistDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
