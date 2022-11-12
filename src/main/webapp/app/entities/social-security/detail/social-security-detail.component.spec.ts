import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SocialSecurityDetailComponent } from './social-security-detail.component';

describe('SocialSecurity Management Detail Component', () => {
  let comp: SocialSecurityDetailComponent;
  let fixture: ComponentFixture<SocialSecurityDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SocialSecurityDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ socialSecurity: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SocialSecurityDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SocialSecurityDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load socialSecurity on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.socialSecurity).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
