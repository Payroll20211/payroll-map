import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProjectMasterService } from '../service/project-master.service';
import { IProjectMaster, ProjectMaster } from '../project-master.model';
import { ICostCenter } from 'app/entities/cost-center/cost-center.model';
import { CostCenterService } from 'app/entities/cost-center/service/cost-center.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

import { ProjectMasterUpdateComponent } from './project-master-update.component';

describe('ProjectMaster Management Update Component', () => {
  let comp: ProjectMasterUpdateComponent;
  let fixture: ComponentFixture<ProjectMasterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let projectMasterService: ProjectMasterService;
  let costCenterService: CostCenterService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProjectMasterUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ProjectMasterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProjectMasterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    projectMasterService = TestBed.inject(ProjectMasterService);
    costCenterService = TestBed.inject(CostCenterService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CostCenter query and add missing value', () => {
      const projectMaster: IProjectMaster = { id: 456 };
      const costCenter: ICostCenter = { id: 81291 };
      projectMaster.costCenter = costCenter;

      const costCenterCollection: ICostCenter[] = [{ id: 98087 }];
      jest.spyOn(costCenterService, 'query').mockReturnValue(of(new HttpResponse({ body: costCenterCollection })));
      const additionalCostCenters = [costCenter];
      const expectedCollection: ICostCenter[] = [...additionalCostCenters, ...costCenterCollection];
      jest.spyOn(costCenterService, 'addCostCenterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ projectMaster });
      comp.ngOnInit();

      expect(costCenterService.query).toHaveBeenCalled();
      expect(costCenterService.addCostCenterToCollectionIfMissing).toHaveBeenCalledWith(costCenterCollection, ...additionalCostCenters);
      expect(comp.costCentersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const projectMaster: IProjectMaster = { id: 456 };
      const employee: IEmployee = { id: 72082 };
      projectMaster.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 78721 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ projectMaster });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(employeeCollection, ...additionalEmployees);
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const projectMaster: IProjectMaster = { id: 456 };
      const costCenter: ICostCenter = { id: 99208 };
      projectMaster.costCenter = costCenter;
      const employee: IEmployee = { id: 50705 };
      projectMaster.employee = employee;

      activatedRoute.data = of({ projectMaster });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(projectMaster));
      expect(comp.costCentersSharedCollection).toContain(costCenter);
      expect(comp.employeesSharedCollection).toContain(employee);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ProjectMaster>>();
      const projectMaster = { id: 123 };
      jest.spyOn(projectMasterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ projectMaster });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: projectMaster }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(projectMasterService.update).toHaveBeenCalledWith(projectMaster);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ProjectMaster>>();
      const projectMaster = new ProjectMaster();
      jest.spyOn(projectMasterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ projectMaster });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: projectMaster }));
      saveSubject.complete();

      // THEN
      expect(projectMasterService.create).toHaveBeenCalledWith(projectMaster);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ProjectMaster>>();
      const projectMaster = { id: 123 };
      jest.spyOn(projectMasterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ projectMaster });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(projectMasterService.update).toHaveBeenCalledWith(projectMaster);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCostCenterById', () => {
      it('Should return tracked CostCenter primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCostCenterById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackEmployeeById', () => {
      it('Should return tracked Employee primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEmployeeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
