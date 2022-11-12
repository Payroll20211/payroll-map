import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PositionArlService } from '../service/position-arl.service';

import { PositionArlComponent } from './position-arl.component';

describe('PositionArl Management Component', () => {
  let comp: PositionArlComponent;
  let fixture: ComponentFixture<PositionArlComponent>;
  let service: PositionArlService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PositionArlComponent],
    })
      .overrideTemplate(PositionArlComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PositionArlComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PositionArlService);

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
    expect(comp.positionArls?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
