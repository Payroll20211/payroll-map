import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SocialPaymentsService } from '../service/social-payments.service';
import { ISocialPayments, SocialPayments } from '../social-payments.model';

import { SocialPaymentsUpdateComponent } from './social-payments-update.component';

describe('SocialPayments Management Update Component', () => {
  let comp: SocialPaymentsUpdateComponent;
  let fixture: ComponentFixture<SocialPaymentsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let socialPaymentsService: SocialPaymentsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SocialPaymentsUpdateComponent],
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
      .overrideTemplate(SocialPaymentsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SocialPaymentsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    socialPaymentsService = TestBed.inject(SocialPaymentsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const socialPayments: ISocialPayments = { id: 456 };

      activatedRoute.data = of({ socialPayments });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(socialPayments));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SocialPayments>>();
      const socialPayments = { id: 123 };
      jest.spyOn(socialPaymentsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ socialPayments });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: socialPayments }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(socialPaymentsService.update).toHaveBeenCalledWith(socialPayments);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SocialPayments>>();
      const socialPayments = new SocialPayments();
      jest.spyOn(socialPaymentsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ socialPayments });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: socialPayments }));
      saveSubject.complete();

      // THEN
      expect(socialPaymentsService.create).toHaveBeenCalledWith(socialPayments);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SocialPayments>>();
      const socialPayments = { id: 123 };
      jest.spyOn(socialPaymentsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ socialPayments });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(socialPaymentsService.update).toHaveBeenCalledWith(socialPayments);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
