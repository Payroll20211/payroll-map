import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OperatorTypeComponent } from './list/operator-type.component';
import { OperatorTypeDetailComponent } from './detail/operator-type-detail.component';
import { OperatorTypeUpdateComponent } from './update/operator-type-update.component';
import { OperatorTypeDeleteDialogComponent } from './delete/operator-type-delete-dialog.component';
import { OperatorTypeRoutingModule } from './route/operator-type-routing.module';

@NgModule({
  imports: [SharedModule, OperatorTypeRoutingModule],
  declarations: [OperatorTypeComponent, OperatorTypeDetailComponent, OperatorTypeUpdateComponent, OperatorTypeDeleteDialogComponent],
  entryComponents: [OperatorTypeDeleteDialogComponent],
})
export class OperatorTypeModule {}
