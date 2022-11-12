import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ProjectMasterService } from '../service/project-master.service';

import { ProjectMasterComponent } from './project-master.component';

describe('ProjectMaster Management Component', () => {
  let comp: ProjectMasterComponent;
  let fixture: ComponentFixture<ProjectMasterComponent>;
  let service: ProjectMasterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ProjectMasterComponent],
    })
      .overrideTemplate(ProjectMasterComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProjectMasterComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ProjectMasterService);

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
    expect(comp.projectMasters?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
