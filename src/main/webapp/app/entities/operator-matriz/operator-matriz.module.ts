import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OperatorMatrizComponent } from './list/operator-matriz.component';
import { OperatorMatrizDetailComponent } from './detail/operator-matriz-detail.component';
import { OperatorMatrizUpdateComponent } from './update/operator-matriz-update.component';
import { OperatorMatrizDeleteDialogComponent } from './delete/operator-matriz-delete-dialog.component';
import { OperatorMatrizRoutingModule } from './route/operator-matriz-routing.module';

@NgModule({
  imports: [SharedModule, OperatorMatrizRoutingModule],
  declarations: [
    OperatorMatrizComponent,
    OperatorMatrizDetailComponent,
    OperatorMatrizUpdateComponent,
    OperatorMatrizDeleteDialogComponent,
  ],
  entryComponents: [OperatorMatrizDeleteDialogComponent],
})
export class OperatorMatrizModule {}
