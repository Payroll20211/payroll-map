import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IIncome, Income } from '../income.model';
import { IncomeService } from '../service/income.service';
import { IAccountPlan } from 'app/entities/account-plan/account-plan.model';
import { AccountPlanService } from 'app/entities/account-plan/service/account-plan.service';

@Component({
  selector: 'payroll-income-update',
  templateUrl: './income-update.component.html',
})
export class IncomeUpdateComponent implements OnInit {
  isSaving = false;

  accountPlansSharedCollection: IAccountPlan[] = [];

  editForm = this.fb.group({
    id: [],
    incomeCode: [null, [Validators.required, Validators.maxLength(10)]],
    description: [null, [Validators.required, Validators.maxLength(100)]],
    accountPlans: [],
  });

  constructor(
    protected incomeService: IncomeService,
    protected accountPlanService: AccountPlanService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ income }) => {
      this.updateForm(income);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const income = this.createFromForm();
    if (income.id !== undefined) {
      this.subscribeToSaveResponse(this.incomeService.update(income));
    } else {
      this.subscribeToSaveResponse(this.incomeService.create(income));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIncome>>): void {
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

  protected updateForm(income: IIncome): void {
    this.editForm.patchValue({
      id: income.id,
      incomeCode: income.incomeCode,
      description: income.description,
      accountPlans: income.accountPlans,
    });

    this.accountPlansSharedCollection = this.accountPlanService.addAccountPlanToCollectionIfMissing(
      this.accountPlansSharedCollection,
      ...(income.accountPlans ?? [])
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

  protected createFromForm(): IIncome {
    return {
      ...new Income(),
      id: this.editForm.get(['id'])!.value,
      incomeCode: this.editForm.get(['incomeCode'])!.value,
      description: this.editForm.get(['description'])!.value,
      accountPlans: this.editForm.get(['accountPlans'])!.value,
    };
  }
}
