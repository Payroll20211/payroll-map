import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { OperatorTypeService } from '../service/operator-type.service';

import { OperatorTypeComponent } from './operator-type.component';

describe('OperatorType Management Component', () => {
  let comp: OperatorTypeComponent;
  let fixture: ComponentFixture<OperatorTypeComponent>;
  let service: OperatorTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [OperatorTypeComponent],
    })
      .overrideTemplate(OperatorTypeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OperatorTypeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OperatorTypeService);

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
    expect(comp.operatorTypes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
