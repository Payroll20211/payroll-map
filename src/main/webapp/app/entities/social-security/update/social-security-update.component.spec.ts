import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SocialSecurityService } from '../service/social-security.service';
import { ISocialSecurity, SocialSecurity } from '../social-security.model';

import { SocialSecurityUpdateComponent } from './social-security-update.component';

describe('SocialSecurity Management Update Component', () => {
  let comp: SocialSecurityUpdateComponent;
  let fixture: ComponentFixture<SocialSecurityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let socialSecurityService: SocialSecurityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SocialSecurityUpdateComponent],
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
      .overrideTemplate(SocialSecurityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SocialSecurityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    socialSecurityService = TestBed.inject(SocialSecurityService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const socialSecurity: ISocialSecurity = { id: 456 };

      activatedRoute.data = of({ socialSecurity });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(socialSecurity));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SocialSecurity>>();
      const socialSecurity = { id: 123 };
      jest.spyOn(socialSecurityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ socialSecurity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: socialSecurity }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(socialSecurityService.update).toHaveBeenCalledWith(socialSecurity);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SocialSecurity>>();
      const socialSecurity = new SocialSecurity();
      jest.spyOn(socialSecurityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ socialSecurity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: socialSecurity }));
      saveSubject.complete();

      // THEN
      expect(socialSecurityService.create).toHaveBeenCalledWith(socialSecurity);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SocialSecurity>>();
      const socialSecurity = { id: 123 };
      jest.spyOn(socialSecurityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ socialSecurity });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(socialSecurityService.update).toHaveBeenCalledWith(socialSecurity);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
