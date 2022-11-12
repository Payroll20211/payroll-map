import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AccountPlanService } from '../service/account-plan.service';

import { AccountPlanComponent } from './account-plan.component';

describe('AccountPlan Management Component', () => {
  let comp: AccountPlanComponent;
  let fixture: ComponentFixture<AccountPlanComponent>;
  let service: AccountPlanService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AccountPlanComponent],
    })
      .overrideTemplate(AccountPlanComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccountPlanComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AccountPlanService);

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
    expect(comp.accountPlans?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
