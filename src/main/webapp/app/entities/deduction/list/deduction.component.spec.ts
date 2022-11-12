import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DeductionService } from '../service/deduction.service';

import { DeductionComponent } from './deduction.component';

describe('Deduction Management Component', () => {
  let comp: DeductionComponent;
  let fixture: ComponentFixture<DeductionComponent>;
  let service: DeductionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DeductionComponent],
    })
      .overrideTemplate(DeductionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeductionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DeductionService);

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
    expect(comp.deductions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
