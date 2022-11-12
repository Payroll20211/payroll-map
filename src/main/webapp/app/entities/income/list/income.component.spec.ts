import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { IncomeService } from '../service/income.service';

import { IncomeComponent } from './income.component';

describe('Income Management Component', () => {
  let comp: IncomeComponent;
  let fixture: ComponentFixture<IncomeComponent>;
  let service: IncomeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [IncomeComponent],
    })
      .overrideTemplate(IncomeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IncomeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(IncomeService);

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
    expect(comp.incomes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
