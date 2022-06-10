import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SocialPaymentsService } from '../service/social-payments.service';

import { SocialPaymentsComponent } from './social-payments.component';

describe('SocialPayments Management Component', () => {
  let comp: SocialPaymentsComponent;
  let fixture: ComponentFixture<SocialPaymentsComponent>;
  let service: SocialPaymentsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SocialPaymentsComponent],
    })
      .overrideTemplate(SocialPaymentsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SocialPaymentsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SocialPaymentsService);

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
    expect(comp.socialPayments?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
