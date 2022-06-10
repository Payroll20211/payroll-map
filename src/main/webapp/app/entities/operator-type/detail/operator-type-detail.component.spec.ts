import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OperatorTypeDetailComponent } from './operator-type-detail.component';

describe('OperatorType Management Detail Component', () => {
  let comp: OperatorTypeDetailComponent;
  let fixture: ComponentFixture<OperatorTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OperatorTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ operatorType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OperatorTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OperatorTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load operatorType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.operatorType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
