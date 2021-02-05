import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditAlbumDialogComponent } from './edit-album-dialog.component';

describe('EditAlbumDialogComponent', () => {
  let component: EditAlbumDialogComponent;
  let fixture: ComponentFixture<EditAlbumDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditAlbumDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditAlbumDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
