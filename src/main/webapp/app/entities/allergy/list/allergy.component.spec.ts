import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AllergyService } from '../service/allergy.service';

import { AllergyComponent } from './allergy.component';

describe('Allergy Management Component', () => {
  let comp: AllergyComponent;
  let fixture: ComponentFixture<AllergyComponent>;
  let service: AllergyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AllergyComponent],
    })
      .overrideTemplate(AllergyComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AllergyComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AllergyService);

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
    expect(comp.allergies?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
