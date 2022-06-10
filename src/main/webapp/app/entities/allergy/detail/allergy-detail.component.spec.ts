import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AllergyDetailComponent } from './allergy-detail.component';

describe('Allergy Management Detail Component', () => {
  let comp: AllergyDetailComponent;
  let fixture: ComponentFixture<AllergyDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AllergyDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ allergy: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AllergyDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AllergyDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load allergy on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.allergy).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
