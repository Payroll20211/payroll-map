import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICostCenter, CostCenter } from '../cost-center.model';
import { CostCenterService } from '../service/cost-center.service';

@Component({
  selector: 'payroll-cost-center-update',
  templateUrl: './cost-center-update.component.html',
})
export class CostCenterUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    costCenterCode: [null, [Validators.required, Validators.maxLength(10)]],
    costCenterName: [null, [Validators.required, Validators.maxLength(100)]],
    costCenterType: [null, [Validators.required, Validators.maxLength(100)]],
  });

  constructor(protected costCenterService: CostCenterService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ costCenter }) => {
      this.updateForm(costCenter);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const costCenter = this.createFromForm();
    if (costCenter.id !== undefined) {
      this.subscribeToSaveResponse(this.costCenterService.update(costCenter));
    } else {
      this.subscribeToSaveResponse(this.costCenterService.create(costCenter));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICostCenter>>): void {
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

  protected updateForm(costCenter: ICostCenter): void {
    this.editForm.patchValue({
      id: costCenter.id,
      costCenterCode: costCenter.costCenterCode,
      costCenterName: costCenter.costCenterName,
      costCenterType: costCenter.costCenterType,
    });
  }

  protected createFromForm(): ICostCenter {
    return {
      ...new CostCenter(),
      id: this.editForm.get(['id'])!.value,
      costCenterCode: this.editForm.get(['costCenterCode'])!.value,
      costCenterName: this.editForm.get(['costCenterName'])!.value,
      costCenterType: this.editForm.get(['costCenterType'])!.value,
    };
  }
}
