import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IOperatorMatriz, OperatorMatriz } from '../operator-matriz.model';
import { OperatorMatrizService } from '../service/operator-matriz.service';

@Component({
  selector: 'payroll-operator-matriz-update',
  templateUrl: './operator-matriz-update.component.html',
})
export class OperatorMatrizUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    numberid: [],
    digitverification: [],
    name: [null, [Validators.required, Validators.maxLength(100)]],
    address: [null, [Validators.required, Validators.maxLength(100)]],
    city: [null, [Validators.required, Validators.maxLength(100)]],
    email: [null, [Validators.required, Validators.maxLength(100)]],
  });

  constructor(
    protected operatorMatrizService: OperatorMatrizService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ operatorMatriz }) => {
      this.updateForm(operatorMatriz);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const operatorMatriz = this.createFromForm();
    if (operatorMatriz.id !== undefined) {
      this.subscribeToSaveResponse(this.operatorMatrizService.update(operatorMatriz));
    } else {
      this.subscribeToSaveResponse(this.operatorMatrizService.create(operatorMatriz));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOperatorMatriz>>): void {
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

  protected updateForm(operatorMatriz: IOperatorMatriz): void {
    this.editForm.patchValue({
      id: operatorMatriz.id,
      numberid: operatorMatriz.numberid,
      digitverification: operatorMatriz.digitverification,
      name: operatorMatriz.name,
      address: operatorMatriz.address,
      city: operatorMatriz.city,
      email: operatorMatriz.email,
    });
  }

  protected createFromForm(): IOperatorMatriz {
    return {
      ...new OperatorMatriz(),
      id: this.editForm.get(['id'])!.value,
      numberid: this.editForm.get(['numberid'])!.value,
      digitverification: this.editForm.get(['digitverification'])!.value,
      name: this.editForm.get(['name'])!.value,
      address: this.editForm.get(['address'])!.value,
      city: this.editForm.get(['city'])!.value,
      email: this.editForm.get(['email'])!.value,
    };
  }
}
