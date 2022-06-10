import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAccountPlan } from '../account-plan.model';
import { AccountPlanService } from '../service/account-plan.service';
import { AccountPlanDeleteDialogComponent } from '../delete/account-plan-delete-dialog.component';

@Component({
  selector: 'payroll-account-plan',
  templateUrl: './account-plan.component.html',
})
export class AccountPlanComponent implements OnInit {
  accountPlans?: IAccountPlan[];
  isLoading = false;

  constructor(protected accountPlanService: AccountPlanService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.accountPlanService.query().subscribe({
      next: (res: HttpResponse<IAccountPlan[]>) => {
        this.isLoading = false;
        this.accountPlans = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IAccountPlan): number {
    return item.id!;
  }

  delete(accountPlan: IAccountPlan): void {
    const modalRef = this.modalService.open(AccountPlanDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.accountPlan = accountPlan;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
