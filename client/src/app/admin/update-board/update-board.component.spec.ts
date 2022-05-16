import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateBoardComponent } from './update-board.component';

describe('UpdateBoardComponent', () => {
  let component: UpdateBoardComponent;
  let fixture: ComponentFixture<UpdateBoardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateBoardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateBoardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
