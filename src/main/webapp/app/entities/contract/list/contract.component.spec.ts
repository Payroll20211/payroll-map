import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ContractService } from '../service/contract.service';

import { ContractComponent } from './contract.component';

describe('Contract Management Component', () => {
  let comp: ContractComponent;
  let fixture: ComponentFixture<ContractComponent>;
  let service: ContractService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ContractComponent],
    })
      .overrideTemplate(ContractComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContractComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ContractService);

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
    expect(comp.contracts?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
