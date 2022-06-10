import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISocialSecurity, SocialSecurity } from '../social-security.model';
import { SocialSecurityService } from '../service/social-security.service';

@Component({
  selector: 'payroll-social-security-update',
  templateUrl: './social-security-update.component.html',
})
export class SocialSecurityUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    eps: [null, [Validators.required, Validators.maxLength(100)]],
    afp: [null, [Validators.required, Validators.maxLength(100)]],
  });

  constructor(
    protected socialSecurityService: SocialSecurityService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ socialSecurity }) => {
      this.updateForm(socialSecurity);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const socialSecurity = this.createFromForm();
    if (socialSecurity.id !== undefined) {
      this.subscribeToSaveResponse(this.socialSecurityService.update(socialSecurity));
    } else {
      this.subscribeToSaveResponse(this.socialSecurityService.create(socialSecurity));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISocialSecurity>>): void {
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

  protected updateForm(socialSecurity: ISocialSecurity): void {
    this.editForm.patchValue({
      id: socialSecurity.id,
      eps: socialSecurity.eps,
      afp: socialSecurity.afp,
    });
  }

  protected createFromForm(): ISocialSecurity {
    return {
      ...new SocialSecurity(),
      id: this.editForm.get(['id'])!.value,
      eps: this.editForm.get(['eps'])!.value,
      afp: this.editForm.get(['afp'])!.value,
    };
  }
}
