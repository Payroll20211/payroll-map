import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISocialPayments, SocialPayments } from '../social-payments.model';
import { SocialPaymentsService } from '../service/social-payments.service';

@Component({
  selector: 'payroll-social-payments-update',
  templateUrl: './social-payments-update.component.html',
})
export class SocialPaymentsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    code: [null, [Validators.required, Validators.maxLength(10)]],
    description: [null, [Validators.required, Validators.maxLength(100)]],
  });

  constructor(
    protected socialPaymentsService: SocialPaymentsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ socialPayments }) => {
      this.updateForm(socialPayments);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const socialPayments = this.createFromForm();
    if (socialPayments.id !== undefined) {
      this.subscribeToSaveResponse(this.socialPaymentsService.update(socialPayments));
    } else {
      this.subscribeToSaveResponse(this.socialPaymentsService.create(socialPayments));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISocialPayments>>): void {
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

  protected updateForm(socialPayments: ISocialPayments): void {
    this.editForm.patchValue({
      id: socialPayments.id,
      code: socialPayments.code,
      description: socialPayments.description,
    });
  }

  protected createFromForm(): ISocialPayments {
    return {
      ...new SocialPayments(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
