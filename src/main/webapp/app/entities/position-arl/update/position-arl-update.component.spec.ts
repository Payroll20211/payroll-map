import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PositionArlService } from '../service/position-arl.service';
import { IPositionArl, PositionArl } from '../position-arl.model';

import { PositionArlUpdateComponent } from './position-arl-update.component';

describe('PositionArl Management Update Component', () => {
  let comp: PositionArlUpdateComponent;
  let fixture: ComponentFixture<PositionArlUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let positionArlService: PositionArlService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PositionArlUpdateComponent],
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
      .overrideTemplate(PositionArlUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PositionArlUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    positionArlService = TestBed.inject(PositionArlService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const positionArl: IPositionArl = { id: 456 };

      activatedRoute.data = of({ positionArl });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(positionArl));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PositionArl>>();
      const positionArl = { id: 123 };
      jest.spyOn(positionArlService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ positionArl });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: positionArl }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(positionArlService.update).toHaveBeenCalledWith(positionArl);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PositionArl>>();
      const positionArl = new PositionArl();
      jest.spyOn(positionArlService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ positionArl });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: positionArl }));
      saveSubject.complete();

      // THEN
      expect(positionArlService.create).toHaveBeenCalledWith(positionArl);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PositionArl>>();
      const positionArl = { id: 123 };
      jest.spyOn(positionArlService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ positionArl });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(positionArlService.update).toHaveBeenCalledWith(positionArl);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
