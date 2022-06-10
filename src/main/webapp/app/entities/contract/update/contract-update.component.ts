import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IContract, Contract } from '../contract.model';
import { ContractService } from '../service/contract.service';

@Component({
  selector: 'payroll-contract-update',
  templateUrl: './contract-update.component.html',
})
export class ContractUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    salary: [null, [Validators.required]],
  });

  constructor(protected contractService: ContractService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contract }) => {
      this.updateForm(contract);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contract = this.createFromForm();
    if (contract.id !== undefined) {
      this.subscribeToSaveResponse(this.contractService.update(contract));
    } else {
      this.subscribeToSaveResponse(this.contractService.create(contract));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContract>>): void {
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

  protected updateForm(contract: IContract): void {
    this.editForm.patchValue({
      id: contract.id,
      salary: contract.salary,
    });
  }

  protected createFromForm(): IContract {
    return {
      ...new Contract(),
      id: this.editForm.get(['id'])!.value,
      salary: this.editForm.get(['salary'])!.value,
    };
  }
}
