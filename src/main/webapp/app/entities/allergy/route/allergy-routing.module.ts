import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AllergyComponent } from '../list/allergy.component';
import { AllergyDetailComponent } from '../detail/allergy-detail.component';
import { AllergyUpdateComponent } from '../update/allergy-update.component';
import { AllergyRoutingResolveService } from './allergy-routing-resolve.service';

const allergyRoute: Routes = [
  {
    path: '',
    component: AllergyComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AllergyDetailComponent,
    resolve: {
      allergy: AllergyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AllergyUpdateComponent,
    resolve: {
      allergy: AllergyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AllergyUpdateComponent,
    resolve: {
      allergy: AllergyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(allergyRoute)],
  exports: [RouterModule],
})
export class AllergyRoutingModule {}
