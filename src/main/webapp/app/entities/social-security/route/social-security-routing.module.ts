import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SocialSecurityComponent } from '../list/social-security.component';
import { SocialSecurityDetailComponent } from '../detail/social-security-detail.component';
import { SocialSecurityUpdateComponent } from '../update/social-security-update.component';
import { SocialSecurityRoutingResolveService } from './social-security-routing-resolve.service';

const socialSecurityRoute: Routes = [
  {
    path: '',
    component: SocialSecurityComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SocialSecurityDetailComponent,
    resolve: {
      socialSecurity: SocialSecurityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SocialSecurityUpdateComponent,
    resolve: {
      socialSecurity: SocialSecurityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SocialSecurityUpdateComponent,
    resolve: {
      socialSecurity: SocialSecurityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(socialSecurityRoute)],
  exports: [RouterModule],
})
export class SocialSecurityRoutingModule {}
