import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SocialPaymentsComponent } from '../list/social-payments.component';
import { SocialPaymentsDetailComponent } from '../detail/social-payments-detail.component';
import { SocialPaymentsUpdateComponent } from '../update/social-payments-update.component';
import { SocialPaymentsRoutingResolveService } from './social-payments-routing-resolve.service';

const socialPaymentsRoute: Routes = [
  {
    path: '',
    component: SocialPaymentsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SocialPaymentsDetailComponent,
    resolve: {
      socialPayments: SocialPaymentsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SocialPaymentsUpdateComponent,
    resolve: {
      socialPayments: SocialPaymentsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SocialPaymentsUpdateComponent,
    resolve: {
      socialPayments: SocialPaymentsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(socialPaymentsRoute)],
  exports: [RouterModule],
})
export class SocialPaymentsRoutingModule {}
