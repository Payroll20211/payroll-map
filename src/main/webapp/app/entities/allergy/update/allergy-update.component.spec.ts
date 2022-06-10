import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AllergyService } from '../service/allergy.service';
import { IAllergy, Allergy } from '../allergy.model';

import { AllergyUpdateComponent } from './allergy-update.component';

describe('Allergy Management Update Component', () => {
  let comp: AllergyUpdateComponent;
  let fixture: ComponentFixture<AllergyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let allergyService: AllergyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AllergyUpdateComponent],
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
      .overrideTemplate(AllergyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AllergyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    allergyService = TestBed.inject(AllergyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const allergy: IAllergy = { id: 456 };

      activatedRoute.data = of({ allergy });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(allergy));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Allergy>>();
      const allergy = { id: 123 };
      jest.spyOn(allergyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ allergy });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: allergy }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(allergyService.update).toHaveBeenCalledWith(allergy);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Allergy>>();
      const allergy = new Allergy();
      jest.spyOn(allergyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ allergy });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: allergy }));
      saveSubject.complete();

      // THEN
      expect(allergyService.create).toHaveBeenCalledWith(allergy);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Allergy>>();
      const allergy = { id: 123 };
      jest.spyOn(allergyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ allergy });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(allergyService.update).toHaveBeenCalledWith(allergy);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
