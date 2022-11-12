import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PositionArlComponent } from '../list/position-arl.component';
import { PositionArlDetailComponent } from '../detail/position-arl-detail.component';
import { PositionArlUpdateComponent } from '../update/position-arl-update.component';
import { PositionArlRoutingResolveService } from './position-arl-routing-resolve.service';

const positionArlRoute: Routes = [
  {
    path: '',
    component: PositionArlComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PositionArlDetailComponent,
    resolve: {
      positionArl: PositionArlRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PositionArlUpdateComponent,
    resolve: {
      positionArl: PositionArlRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PositionArlUpdateComponent,
    resolve: {
      positionArl: PositionArlRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(positionArlRoute)],
  exports: [RouterModule],
})
export class PositionArlRoutingModule {}
