import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAccountPlan } from '../account-plan.model';

@Component({
  selector: 'payroll-account-plan-detail',
  templateUrl: './account-plan-detail.component.html',
})
export class AccountPlanDetailComponent implements OnInit {
  accountPlan: IAccountPlan | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accountPlan }) => {
      this.accountPlan = accountPlan;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
