import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProjectMaster, ProjectMaster } from '../project-master.model';
import { ProjectMasterService } from '../service/project-master.service';
import { ICostCenter } from 'app/entities/cost-center/cost-center.model';
import { CostCenterService } from 'app/entities/cost-center/service/cost-center.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

@Component({
  selector: 'payroll-project-master-update',
  templateUrl: './project-master-update.component.html',
})
export class ProjectMasterUpdateComponent implements OnInit {
  isSaving = false;

  costCentersSharedCollection: ICostCenter[] = [];
  employeesSharedCollection: IEmployee[] = [];

  editForm = this.fb.group({
    id: [],
    projectMasterCode: [null, [Validators.required, Validators.maxLength(10)]],
    projectMasterName: [null, [Validators.required, Validators.maxLength(100)]],
    costCenterType: [null, [Validators.required, Validators.maxLength(100)]],
    projectDirectorName: [null, [Validators.required, Validators.maxLength(100)]],
    phone: [null, [Validators.required, Validators.maxLength(100)]],
    costCenter: [null, Validators.required],
    employee: [],
  });

  constructor(
    protected projectMasterService: ProjectMasterService,
    protected costCenterService: CostCenterService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ projectMaster }) => {
      this.updateForm(projectMaster);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const projectMaster = this.createFromForm();
    if (projectMaster.id !== undefined) {
      this.subscribeToSaveResponse(this.projectMasterService.update(projectMaster));
    } else {
      this.subscribeToSaveResponse(this.projectMasterService.create(projectMaster));
    }
  }

  trackCostCenterById(_index: number, item: ICostCenter): number {
    return item.id!;
  }

  trackEmployeeById(_index: number, item: IEmployee): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProjectMaster>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(projectMaster: IProjectMaster): void {
    this.editForm.patchValue({
      id: projectMaster.id,
      projectMasterCode: projectMaster.projectMasterCode,
      projectMasterName: projectMaster.projectMasterName,
      costCenterType: projectMaster.costCenterType,
      projectDirectorName: projectMaster.projectDirectorName,
      phone: projectMaster.phone,
      costCenter: projectMaster.costCenter,
      employee: projectMaster.employee,
    });

    this.costCentersSharedCollection = this.costCenterService.addCostCenterToCollectionIfMissing(
      this.costCentersSharedCollection,
      projectMaster.costCenter
    );
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing(
      this.employeesSharedCollection,
      projectMaster.employee
    );
  }

  protected loadRelationshipsOptions(): void {
    this.costCenterService
      .query()
      .pipe(map((res: HttpResponse<ICostCenter[]>) => res.body ?? []))
      .pipe(
        map((costCenters: ICostCenter[]) =>
          this.costCenterService.addCostCenterToCollectionIfMissing(costCenters, this.editForm.get('costCenter')!.value)
        )
      )
      .subscribe((costCenters: ICostCenter[]) => (this.costCentersSharedCollection = costCenters));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing(employees, this.editForm.get('employee')!.value)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }

  protected createFromForm(): IProjectMaster {
    return {
      ...new ProjectMaster(),
      id: this.editForm.get(['id'])!.value,
      projectMasterCode: this.editForm.get(['projectMasterCode'])!.value,
      projectMasterName: this.editForm.get(['projectMasterName'])!.value,
      costCenterType: this.editForm.get(['costCenterType'])!.value,
      projectDirectorName: this.editForm.get(['projectDirectorName'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      costCenter: this.editForm.get(['costCenter'])!.value,
      employee: this.editForm.get(['employee'])!.value,
    };
  }
}
