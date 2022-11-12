import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OperatorMatrizService } from '../service/operator-matriz.service';
import { IOperatorMatriz, OperatorMatriz } from '../operator-matriz.model';

import { OperatorMatrizUpdateComponent } from './operator-matriz-update.component';

describe('OperatorMatriz Management Update Component', () => {
  let comp: OperatorMatrizUpdateComponent;
  let fixture: ComponentFixture<OperatorMatrizUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let operatorMatrizService: OperatorMatrizService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OperatorMatrizUpdateComponent],
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
      .overrideTemplate(OperatorMatrizUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OperatorMatrizUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    operatorMatrizService = TestBed.inject(OperatorMatrizService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const operatorMatriz: IOperatorMatriz = { id: 456 };

      activatedRoute.data = of({ operatorMatriz });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(operatorMatriz));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OperatorMatriz>>();
      const operatorMatriz = { id: 123 };
      jest.spyOn(operatorMatrizService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operatorMatriz });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operatorMatriz }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(operatorMatrizService.update).toHaveBeenCalledWith(operatorMatriz);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OperatorMatriz>>();
      const operatorMatriz = new OperatorMatriz();
      jest.spyOn(operatorMatrizService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operatorMatriz });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operatorMatriz }));
      saveSubject.complete();

      // THEN
      expect(operatorMatrizService.create).toHaveBeenCalledWith(operatorMatriz);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OperatorMatriz>>();
      const operatorMatriz = { id: 123 };
      jest.spyOn(operatorMatrizService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operatorMatriz });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(operatorMatrizService.update).toHaveBeenCalledWith(operatorMatriz);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
