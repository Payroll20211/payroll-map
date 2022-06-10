import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IOperatorType, OperatorType } from '../operator-type.model';
import { OperatorTypeService } from '../service/operator-type.service';

@Component({
  selector: 'payroll-operator-type-update',
  templateUrl: './operator-type-update.component.html',
})
export class OperatorTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    code: [null, [Validators.required, Validators.maxLength(10)]],
    description: [null, [Validators.required, Validators.maxLength(100)]],
  });

  constructor(protected operatorTypeService: OperatorTypeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ operatorType }) => {
      this.updateForm(operatorType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const operatorType = this.createFromForm();
    if (operatorType.id !== undefined) {
      this.subscribeToSaveResponse(this.operatorTypeService.update(operatorType));
    } else {
      this.subscribeToSaveResponse(this.operatorTypeService.create(operatorType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOperatorType>>): void {
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

  protected updateForm(operatorType: IOperatorType): void {
    this.editForm.patchValue({
      id: operatorType.id,
      code: operatorType.code,
      description: operatorType.description,
    });
  }

  protected createFromForm(): IOperatorType {
    return {
      ...new OperatorType(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
