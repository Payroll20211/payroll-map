import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPositionArl, PositionArl } from '../position-arl.model';
import { PositionArlService } from '../service/position-arl.service';

@Component({
  selector: 'payroll-position-arl-update',
  templateUrl: './position-arl-update.component.html',
})
export class PositionArlUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    riskClass: [null, [Validators.required]],
    positionCode: [null, [Validators.required, Validators.maxLength(10)]],
    position: [null, [Validators.required, Validators.maxLength(100)]],
  });

  constructor(protected positionArlService: PositionArlService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ positionArl }) => {
      this.updateForm(positionArl);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const positionArl = this.createFromForm();
    if (positionArl.id !== undefined) {
      this.subscribeToSaveResponse(this.positionArlService.update(positionArl));
    } else {
      this.subscribeToSaveResponse(this.positionArlService.create(positionArl));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPositionArl>>): void {
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

  protected updateForm(positionArl: IPositionArl): void {
    this.editForm.patchValue({
      id: positionArl.id,
      riskClass: positionArl.riskClass,
      positionCode: positionArl.positionCode,
      position: positionArl.position,
    });
  }

  protected createFromForm(): IPositionArl {
    return {
      ...new PositionArl(),
      id: this.editForm.get(['id'])!.value,
      riskClass: this.editForm.get(['riskClass'])!.value,
      positionCode: this.editForm.get(['positionCode'])!.value,
      position: this.editForm.get(['position'])!.value,
    };
  }
}
