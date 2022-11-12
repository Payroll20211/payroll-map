import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AccountPlanComponent } from '../list/account-plan.component';
import { AccountPlanDetailComponent } from '../detail/account-plan-detail.component';
import { AccountPlanUpdateComponent } from '../update/account-plan-update.component';
import { AccountPlanRoutingResolveService } from './account-plan-routing-resolve.service';

const accountPlanRoute: Routes = [
  {
    path: '',
    component: AccountPlanComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AccountPlanDetailComponent,
    resolve: {
      accountPlan: AccountPlanRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AccountPlanUpdateComponent,
    resolve: {
      accountPlan: AccountPlanRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AccountPlanUpdateComponent,
    resolve: {
      accountPlan: AccountPlanRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(accountPlanRoute)],
  exports: [RouterModule],
})
export class AccountPlanRoutingModule {}
