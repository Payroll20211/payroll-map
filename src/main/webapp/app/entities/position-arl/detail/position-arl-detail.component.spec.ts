import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PositionArlDetailComponent } from './position-arl-detail.component';

describe('PositionArl Management Detail Component', () => {
  let comp: PositionArlDetailComponent;
  let fixture: ComponentFixture<PositionArlDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PositionArlDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ positionArl: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PositionArlDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PositionArlDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load positionArl on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.positionArl).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
