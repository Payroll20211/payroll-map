import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CostCenterComponent } from '../list/cost-center.component';
import { CostCenterDetailComponent } from '../detail/cost-center-detail.component';
import { CostCenterUpdateComponent } from '../update/cost-center-update.component';
import { CostCenterRoutingResolveService } from './cost-center-routing-resolve.service';

const costCenterRoute: Routes = [
  {
    path: '',
    component: CostCenterComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CostCenterDetailComponent,
    resolve: {
      costCenter: CostCenterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CostCenterUpdateComponent,
    resolve: {
      costCenter: CostCenterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CostCenterUpdateComponent,
    resolve: {
      costCenter: CostCenterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(costCenterRoute)],
  exports: [RouterModule],
})
export class CostCenterRoutingModule {}
