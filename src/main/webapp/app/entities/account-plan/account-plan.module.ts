import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AccountPlanComponent } from './list/account-plan.component';
import { AccountPlanDetailComponent } from './detail/account-plan-detail.component';
import { AccountPlanUpdateComponent } from './update/account-plan-update.component';
import { AccountPlanDeleteDialogComponent } from './delete/account-plan-delete-dialog.component';
import { AccountPlanRoutingModule } from './route/account-plan-routing.module';

@NgModule({
  imports: [SharedModule, AccountPlanRoutingModule],
  declarations: [AccountPlanComponent, AccountPlanDetailComponent, AccountPlanUpdateComponent, AccountPlanDeleteDialogComponent],
  entryComponents: [AccountPlanDeleteDialogComponent],
})
export class AccountPlanModule {}
