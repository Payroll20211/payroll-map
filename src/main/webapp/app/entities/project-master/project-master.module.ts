import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProjectMasterComponent } from './list/project-master.component';
import { ProjectMasterDetailComponent } from './detail/project-master-detail.component';
import { ProjectMasterUpdateComponent } from './update/project-master-update.component';
import { ProjectMasterDeleteDialogComponent } from './delete/project-master-delete-dialog.component';
import { ProjectMasterRoutingModule } from './route/project-master-routing.module';

@NgModule({
  imports: [SharedModule, ProjectMasterRoutingModule],
  declarations: [ProjectMasterComponent, ProjectMasterDetailComponent, ProjectMasterUpdateComponent, ProjectMasterDeleteDialogComponent],
  entryComponents: [ProjectMasterDeleteDialogComponent],
})
export class ProjectMasterModule {}
