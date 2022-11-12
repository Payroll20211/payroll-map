import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PeriodService } from '../service/period.service';
import { IPeriod, Period } from '../period.model';

import { PeriodUpdateComponent } from './period-update.component';

describe('Period Management Update Component', () => {
  let comp: PeriodUpdateComponent;
  let fixture: ComponentFixture<PeriodUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let periodService: PeriodService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PeriodUpdateComponent],
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
      .overrideTemplate(PeriodUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PeriodUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    periodService = TestBed.inject(PeriodService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const period: IPeriod = { id: 456 };

      activatedRoute.data = of({ period });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(period));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Period>>();
      const period = { id: 123 };
      jest.spyOn(periodService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ period });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: period }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(periodService.update).toHaveBeenCalledWith(period);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Period>>();
      const period = new Period();
      jest.spyOn(periodService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ period });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: period }));
      saveSubject.complete();

      // THEN
      expect(periodService.create).toHaveBeenCalledWith(period);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Period>>();
      const period = { id: 123 };
      jest.spyOn(periodService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ period });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(periodService.update).toHaveBeenCalledWith(period);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
