import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OperatorTypeService } from '../service/operator-type.service';
import { IOperatorType, OperatorType } from '../operator-type.model';

import { OperatorTypeUpdateComponent } from './operator-type-update.component';

describe('OperatorType Management Update Component', () => {
  let comp: OperatorTypeUpdateComponent;
  let fixture: ComponentFixture<OperatorTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let operatorTypeService: OperatorTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OperatorTypeUpdateComponent],
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
      .overrideTemplate(OperatorTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OperatorTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    operatorTypeService = TestBed.inject(OperatorTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const operatorType: IOperatorType = { id: 456 };

      activatedRoute.data = of({ operatorType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(operatorType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OperatorType>>();
      const operatorType = { id: 123 };
      jest.spyOn(operatorTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operatorType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operatorType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(operatorTypeService.update).toHaveBeenCalledWith(operatorType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OperatorType>>();
      const operatorType = new OperatorType();
      jest.spyOn(operatorTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operatorType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operatorType }));
      saveSubject.complete();

      // THEN
      expect(operatorTypeService.create).toHaveBeenCalledWith(operatorType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<OperatorType>>();
      const operatorType = { id: 123 };
      jest.spyOn(operatorTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operatorType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(operatorTypeService.update).toHaveBeenCalledWith(operatorType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
