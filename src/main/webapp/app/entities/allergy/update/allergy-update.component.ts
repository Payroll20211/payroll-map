import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAllergy, Allergy } from '../allergy.model';
import { AllergyService } from '../service/allergy.service';

@Component({
  selector: 'payroll-allergy-update',
  templateUrl: './allergy-update.component.html',
})
export class AllergyUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    description: [null, [Validators.required, Validators.maxLength(100)]],
    treatment: [null, [Validators.required, Validators.maxLength(100)]],
  });

  constructor(protected allergyService: AllergyService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ allergy }) => {
      this.updateForm(allergy);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const allergy = this.createFromForm();
    if (allergy.id !== undefined) {
      this.subscribeToSaveResponse(this.allergyService.update(allergy));
    } else {
      this.subscribeToSaveResponse(this.allergyService.create(allergy));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAllergy>>): void {
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

  protected updateForm(allergy: IAllergy): void {
    this.editForm.patchValue({
      id: allergy.id,
      description: allergy.description,
      treatment: allergy.treatment,
    });
  }

  protected createFromForm(): IAllergy {
    return {
      ...new Allergy(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      treatment: this.editForm.get(['treatment'])!.value,
    };
  }
}
