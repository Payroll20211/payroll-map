import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAccountPlan } from '../account-plan.model';
import { AccountPlanService } from '../service/account-plan.service';

@Component({
  templateUrl: './account-plan-delete-dialog.component.html',
})
export class AccountPlanDeleteDialogComponent {
  accountPlan?: IAccountPlan;

  constructor(protected accountPlanService: AccountPlanService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.accountPlanService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
