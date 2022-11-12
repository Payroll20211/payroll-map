import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CostCenterService } from '../service/cost-center.service';

import { CostCenterComponent } from './cost-center.component';

describe('CostCenter Management Component', () => {
  let comp: CostCenterComponent;
  let fixture: ComponentFixture<CostCenterComponent>;
  let service: CostCenterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CostCenterComponent],
    })
      .overrideTemplate(CostCenterComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CostCenterComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CostCenterService);

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
    expect(comp.costCenters?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
