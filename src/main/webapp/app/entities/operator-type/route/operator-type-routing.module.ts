import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OperatorTypeComponent } from '../list/operator-type.component';
import { OperatorTypeDetailComponent } from '../detail/operator-type-detail.component';
import { OperatorTypeUpdateComponent } from '../update/operator-type-update.component';
import { OperatorTypeRoutingResolveService } from './operator-type-routing-resolve.service';

const operatorTypeRoute: Routes = [
  {
    path: '',
    component: OperatorTypeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OperatorTypeDetailComponent,
    resolve: {
      operatorType: OperatorTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OperatorTypeUpdateComponent,
    resolve: {
      operatorType: OperatorTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OperatorTypeUpdateComponent,
    resolve: {
      operatorType: OperatorTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(operatorTypeRoute)],
  exports: [RouterModule],
})
export class OperatorTypeRoutingModule {}
