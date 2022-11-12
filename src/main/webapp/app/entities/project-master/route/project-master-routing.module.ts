import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProjectMasterComponent } from '../list/project-master.component';
import { ProjectMasterDetailComponent } from '../detail/project-master-detail.component';
import { ProjectMasterUpdateComponent } from '../update/project-master-update.component';
import { ProjectMasterRoutingResolveService } from './project-master-routing-resolve.service';

const projectMasterRoute: Routes = [
  {
    path: '',
    component: ProjectMasterComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProjectMasterDetailComponent,
    resolve: {
      projectMaster: ProjectMasterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProjectMasterUpdateComponent,
    resolve: {
      projectMaster: ProjectMasterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProjectMasterUpdateComponent,
    resolve: {
      projectMaster: ProjectMasterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(projectMasterRoute)],
  exports: [RouterModule],
})
export class ProjectMasterRoutingModule {}
