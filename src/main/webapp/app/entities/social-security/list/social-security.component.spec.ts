import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SocialSecurityService } from '../service/social-security.service';

import { SocialSecurityComponent } from './social-security.component';

describe('SocialSecurity Management Component', () => {
  let comp: SocialSecurityComponent;
  let fixture: ComponentFixture<SocialSecurityComponent>;
  let service: SocialSecurityService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SocialSecurityComponent],
    })
      .overrideTemplate(SocialSecurityComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SocialSecurityComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SocialSecurityService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.socialSecurities?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
