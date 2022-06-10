import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProjectMasterDetailComponent } from './project-master-detail.component';

describe('ProjectMaster Management Detail Component', () => {
  let comp: ProjectMasterDetailComponent;
  let fixture: ComponentFixture<ProjectMasterDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProjectMasterDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ projectMaster: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ProjectMasterDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProjectMasterDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load projectMaster on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.projectMaster).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
