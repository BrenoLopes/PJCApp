import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListAllAlbumsComponent } from './list-all-albums.component';

describe('ListAllComponent', () => {
  let component: ListAllAlbumsComponent;
  let fixture: ComponentFixture<ListAllAlbumsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListAllAlbumsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ListAllAlbumsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
