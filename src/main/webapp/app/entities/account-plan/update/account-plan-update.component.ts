import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAccountPlan, AccountPlan } from '../account-plan.model';
import { AccountPlanService } from '../service/account-plan.service';

@Component({
  selector: 'payroll-account-plan-update',
  templateUrl: './account-plan-update.component.html',
})
export class AccountPlanUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    code: [null, [Validators.required, Validators.maxLength(10)]],
    description: [null, [Validators.required, Validators.maxLength(100)]],
  });

  constructor(protected accountPlanService: AccountPlanService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accountPlan }) => {
      this.updateForm(accountPlan);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const accountPlan = this.createFromForm();
    if (accountPlan.id !== undefined) {
      this.subscribeToSaveResponse(this.accountPlanService.update(accountPlan));
    } else {
      this.subscribeToSaveResponse(this.accountPlanService.create(accountPlan));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAccountPlan>>): void {
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

  protected updateForm(accountPlan: IAccountPlan): void {
    this.editForm.patchValue({
      id: accountPlan.id,
      code: accountPlan.code,
      description: accountPlan.description,
    });
  }

  protected createFromForm(): IAccountPlan {
    return {
      ...new AccountPlan(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
