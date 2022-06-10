import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AllergyComponent } from './list/allergy.component';
import { AllergyDetailComponent } from './detail/allergy-detail.component';
import { AllergyUpdateComponent } from './update/allergy-update.component';
import { AllergyDeleteDialogComponent } from './delete/allergy-delete-dialog.component';
import { AllergyRoutingModule } from './route/allergy-routing.module';

@NgModule({
  imports: [SharedModule, AllergyRoutingModule],
  declarations: [AllergyComponent, AllergyDetailComponent, AllergyUpdateComponent, AllergyDeleteDialogComponent],
  entryComponents: [AllergyDeleteDialogComponent],
})
export class AllergyModule {}
