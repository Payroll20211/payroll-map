import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SocialPaymentsDetailComponent } from './social-payments-detail.component';

describe('SocialPayments Management Detail Component', () => {
  let comp: SocialPaymentsDetailComponent;
  let fixture: ComponentFixture<SocialPaymentsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SocialPaymentsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ socialPayments: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SocialPaymentsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SocialPaymentsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load socialPayments on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.socialPayments).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
