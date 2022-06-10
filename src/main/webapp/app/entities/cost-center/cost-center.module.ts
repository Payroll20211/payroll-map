import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CostCenterComponent } from './list/cost-center.component';
import { CostCenterDetailComponent } from './detail/cost-center-detail.component';
import { CostCenterUpdateComponent } from './update/cost-center-update.component';
import { CostCenterDeleteDialogComponent } from './delete/cost-center-delete-dialog.component';
import { CostCenterRoutingModule } from './route/cost-center-routing.module';

@NgModule({
  imports: [SharedModule, CostCenterRoutingModule],
  declarations: [CostCenterComponent, CostCenterDetailComponent, CostCenterUpdateComponent, CostCenterDeleteDialogComponent],
  entryComponents: [CostCenterDeleteDialogComponent],
})
export class CostCenterModule {}
