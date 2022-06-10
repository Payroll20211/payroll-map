import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DeductionService } from '../service/deduction.service';
import { IDeduction, Deduction } from '../deduction.model';
import { IAccountPlan } from 'app/entities/account-plan/account-plan.model';
import { AccountPlanService } from 'app/entities/account-plan/service/account-plan.service';

import { DeductionUpdateComponent } from './deduction-update.component';

describe('Deduction Management Update Component', () => {
  let comp: DeductionUpdateComponent;
  let fixture: ComponentFixture<DeductionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let deductionService: DeductionService;
  let accountPlanService: AccountPlanService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DeductionUpdateComponent],
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
      .overrideTemplate(DeductionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeductionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    deductionService = TestBed.inject(DeductionService);
    accountPlanService = TestBed.inject(AccountPlanService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AccountPlan query and add missing value', () => {
      const deduction: IDeduction = { id: 456 };
      const accountPlans: IAccountPlan[] = [{ id: 51787 }];
      deduction.accountPlans = accountPlans;

      const accountPlanCollection: IAccountPlan[] = [{ id: 82213 }];
      jest.spyOn(accountPlanService, 'query').mockReturnValue(of(new HttpResponse({ body: accountPlanCollection })));
      const additionalAccountPlans = [...accountPlans];
      const expectedCollection: IAccountPlan[] = [...additionalAccountPlans, ...accountPlanCollection];
      jest.spyOn(accountPlanService, 'addAccountPlanToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ deduction });
      comp.ngOnInit();

      expect(accountPlanService.query).toHaveBeenCalled();
      expect(accountPlanService.addAccountPlanToCollectionIfMissing).toHaveBeenCalledWith(accountPlanCollection, ...additionalAccountPlans);
      expect(comp.accountPlansSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const deduction: IDeduction = { id: 456 };
      const accountPlans: IAccountPlan = { id: 59626 };
      deduction.accountPlans = [accountPlans];

      activatedRoute.data = of({ deduction });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(deduction));
      expect(comp.accountPlansSharedCollection).toContain(accountPlans);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Deduction>>();
      const deduction = { id: 123 };
      jest.spyOn(deductionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deduction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deduction }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(deductionService.update).toHaveBeenCalledWith(deduction);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Deduction>>();
      const deduction = new Deduction();
      jest.spyOn(deductionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deduction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deduction }));
      saveSubject.complete();

      // THEN
      expect(deductionService.create).toHaveBeenCalledWith(deduction);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Deduction>>();
      const deduction = { id: 123 };
      jest.spyOn(deductionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deduction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(deductionService.update).toHaveBeenCalledWith(deduction);
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
