import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CostCenterDetailComponent } from './cost-center-detail.component';

describe('CostCenter Management Detail Component', () => {
  let comp: CostCenterDetailComponent;
  let fixture: ComponentFixture<CostCenterDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CostCenterDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ costCenter: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CostCenterDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CostCenterDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load costCenter on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.costCenter).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
