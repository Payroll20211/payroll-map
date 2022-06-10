import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PositionArlComponent } from './list/position-arl.component';
import { PositionArlDetailComponent } from './detail/position-arl-detail.component';
import { PositionArlUpdateComponent } from './update/position-arl-update.component';
import { PositionArlDeleteDialogComponent } from './delete/position-arl-delete-dialog.component';
import { PositionArlRoutingModule } from './route/position-arl-routing.module';

@NgModule({
  imports: [SharedModule, PositionArlRoutingModule],
  declarations: [PositionArlComponent, PositionArlDetailComponent, PositionArlUpdateComponent, PositionArlDeleteDialogComponent],
  entryComponents: [PositionArlDeleteDialogComponent],
})
export class PositionArlModule {}
