import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDeduction, Deduction } from '../deduction.model';
import { DeductionService } from '../service/deduction.service';
import { IAccountPlan } from 'app/entities/account-plan/account-plan.model';
import { AccountPlanService } from 'app/entities/account-plan/service/account-plan.service';

@Component({
  selector: 'payroll-deduction-update',
  templateUrl: './deduction-update.component.html',
})
export class DeductionUpdateComponent implements OnInit {
  isSaving = false;

  accountPlansSharedCollection: IAccountPlan[] = [];

  editForm = this.fb.group({
    id: [],
    deductionCode: [null, [Validators.required, Validators.maxLength(10)]],
    description: [null, [Validators.required, Validators.maxLength(100)]],
    accountPlans: [],
  });

  constructor(
    protected deductionService: DeductionService,
    protected accountPlanService: AccountPlanService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deduction }) => {
      this.updateForm(deduction);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deduction = this.createFromForm();
    if (deduction.id !== undefined) {
      this.subscribeToSaveResponse(this.deductionService.update(deduction));
    } else {
      this.subscribeToSaveResponse(this.deductionService.create(deduction));
    }
  }

  trackAccountPlanById(_index: number, item: IAccountPlan): number {
    return item.id!;
  }

  getSelectedAccountPlan(option: IAccountPlan, selectedVals?: IAccountPlan[]): IAccountPlan {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeduction>>): void {
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

  protected updateForm(deduction: IDeduction): void {
    this.editForm.patchValue({
      id: deduction.id,
      deductionCode: deduction.deductionCode,
      description: deduction.description,
      accountPlans: deduction.accountPlans,
    });

    this.accountPlansSharedCollection = this.accountPlanService.addAccountPlanToCollectionIfMissing(
      this.accountPlansSharedCollection,
      ...(deduction.accountPlans ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.accountPlanService
      .query()
      .pipe(map((res: HttpResponse<IAccountPlan[]>) => res.body ?? []))
      .pipe(
        map((accountPlans: IAccountPlan[]) =>
          this.accountPlanService.addAccountPlanToCollectionIfMissing(accountPlans, ...(this.editForm.get('accountPlans')!.value ?? []))
        )
      )
      .subscribe((accountPlans: IAccountPlan[]) => (this.accountPlansSharedCollection = accountPlans));
  }

  protected createFromForm(): IDeduction {
    return {
      ...new Deduction(),
      id: this.editForm.get(['id'])!.value,
      deductionCode: this.editForm.get(['deductionCode'])!.value,
      description: this.editForm.get(['description'])!.value,
      accountPlans: this.editForm.get(['accountPlans'])!.value,
    };
  }
}
