import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AccountPlanDetailComponent } from './account-plan-detail.component';

describe('AccountPlan Management Detail Component', () => {
  let comp: AccountPlanDetailComponent;
  let fixture: ComponentFixture<AccountPlanDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AccountPlanDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ accountPlan: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AccountPlanDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AccountPlanDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load accountPlan on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.accountPlan).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
