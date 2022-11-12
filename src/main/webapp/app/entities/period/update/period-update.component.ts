import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPeriod, Period } from '../period.model';
import { PeriodService } from '../service/period.service';

@Component({
  selector: 'payroll-period-update',
  templateUrl: './period-update.component.html',
})
export class PeriodUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    periodCode: [null, [Validators.required, Validators.maxLength(10)]],
    description: [null, [Validators.required, Validators.maxLength(100)]],
  });

  constructor(protected periodService: PeriodService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ period }) => {
      this.updateForm(period);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const period = this.createFromForm();
    if (period.id !== undefined) {
      this.subscribeToSaveResponse(this.periodService.update(period));
    } else {
      this.subscribeToSaveResponse(this.periodService.create(period));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPeriod>>): void {
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

  protected updateForm(period: IPeriod): void {
    this.editForm.patchValue({
      id: period.id,
      periodCode: period.periodCode,
      description: period.description,
    });
  }

  protected createFromForm(): IPeriod {
    return {
      ...new Period(),
      id: this.editForm.get(['id'])!.value,
      periodCode: this.editForm.get(['periodCode'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
