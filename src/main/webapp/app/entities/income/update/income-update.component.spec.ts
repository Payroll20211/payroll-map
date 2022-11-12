import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IncomeService } from '../service/income.service';
import { IIncome, Income } from '../income.model';
import { IAccountPlan } from 'app/entities/account-plan/account-plan.model';
import { AccountPlanService } from 'app/entities/account-plan/service/account-plan.service';

import { IncomeUpdateComponent } from './income-update.component';

describe('Income Management Update Component', () => {
  let comp: IncomeUpdateComponent;
  let fixture: ComponentFixture<IncomeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let incomeService: IncomeService;
  let accountPlanService: AccountPlanService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [IncomeUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(IncomeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IncomeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    incomeService = TestBed.inject(IncomeService);
    accountPlanService = TestBed.inject(AccountPlanService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AccountPlan query and add missing value', () => {
      const income: IIncome = { id: 456 };
      const accountPlans: IAccountPlan[] = [{ id: 32900 }];
      income.accountPlans = accountPlans;

      const accountPlanCollection: IAccountPlan[] = [{ id: 58704 }];
      jest.spyOn(accountPlanService, 'query').mockReturnValue(of(new HttpResponse({ body: accountPlanCollection })));
      const additionalAccountPlans = [...accountPlans];
      const expectedCollection: IAccountPlan[] = [...additionalAccountPlans, ...accountPlanCollection];
      jest.spyOn(accountPlanService, 'addAccountPlanToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ income });
      comp.ngOnInit();

      expect(accountPlanService.query).toHaveBeenCalled();
      expect(accountPlanService.addAccountPlanToCollectionIfMissing).toHaveBeenCalledWith(accountPlanCollection, ...additionalAccountPlans);
      expect(comp.accountPlansSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const income: IIncome = { id: 456 };
      const accountPlans: IAccountPlan = { id: 35245 };
      income.accountPlans = [accountPlans];

      activatedRoute.data = of({ income });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(income));
      expect(comp.accountPlansSharedCollection).toContain(accountPlans);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Income>>();
      const income = { id: 123 };
      jest.spyOn(incomeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ income });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: income }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(incomeService.update).toHaveBeenCalledWith(income);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Income>>();
      const income = new Income();
      jest.spyOn(incomeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ income });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: income }));
      saveSubject.complete();

      // THEN
      expect(incomeService.create).toHaveBeenCalledWith(income);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Income>>();
      const income = { id: 123 };
      jest.spyOn(incomeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ income });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(incomeService.update).toHaveBeenCalledWith(income);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackAccountPlanById', () => {
      it('Should return tracked AccountPlan primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAccountPlanById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedAccountPlan', () => {
      it('Should return option if no AccountPlan is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedAccountPlan(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected AccountPlan for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedAccountPlan(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this AccountPlan is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedAccountPlan(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
