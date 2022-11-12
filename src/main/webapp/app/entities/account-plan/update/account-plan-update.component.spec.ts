import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AccountPlanService } from '../service/account-plan.service';
import { IAccountPlan, AccountPlan } from '../account-plan.model';

import { AccountPlanUpdateComponent } from './account-plan-update.component';

describe('AccountPlan Management Update Component', () => {
  let comp: AccountPlanUpdateComponent;
  let fixture: ComponentFixture<AccountPlanUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let accountPlanService: AccountPlanService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AccountPlanUpdateComponent],
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
      .overrideTemplate(AccountPlanUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccountPlanUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    accountPlanService = TestBed.inject(AccountPlanService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const accountPlan: IAccountPlan = { id: 456 };

      activatedRoute.data = of({ accountPlan });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(accountPlan));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AccountPlan>>();
      const accountPlan = { id: 123 };
      jest.spyOn(accountPlanService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountPlan });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountPlan }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(accountPlanService.update).toHaveBeenCalledWith(accountPlan);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AccountPlan>>();
      const accountPlan = new AccountPlan();
      jest.spyOn(accountPlanService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountPlan });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountPlan }));
      saveSubject.complete();

      // THEN
      expect(accountPlanService.create).toHaveBeenCalledWith(accountPlan);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AccountPlan>>();
      const accountPlan = { id: 123 };
      jest.spyOn(accountPlanService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountPlan });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(accountPlanService.update).toHaveBeenCalledWith(accountPlan);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
