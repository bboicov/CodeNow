import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AirGridComponent } from './air-grid.component';

describe('AirGridComponent', () => {
  let component: AirGridComponent;
  let fixture: ComponentFixture<AirGridComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AirGridComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AirGridComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
