import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { OperatorMatrizService } from '../service/operator-matriz.service';

import { OperatorMatrizComponent } from './operator-matriz.component';

describe('OperatorMatriz Management Component', () => {
  let comp: OperatorMatrizComponent;
  let fixture: ComponentFixture<OperatorMatrizComponent>;
  let service: OperatorMatrizService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [OperatorMatrizComponent],
    })
      .overrideTemplate(OperatorMatrizComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OperatorMatrizComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OperatorMatrizService);

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
    expect(comp.operatorMatrizs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
