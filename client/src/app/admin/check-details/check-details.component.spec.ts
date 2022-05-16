import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckDetailsComponent } from './check-details.component';

describe('CheckDetailsComponent', () => {
  let component: CheckDetailsComponent;
  let fixture: ComponentFixture<CheckDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CheckDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CheckDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
