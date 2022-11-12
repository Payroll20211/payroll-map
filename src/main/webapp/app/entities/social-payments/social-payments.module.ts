import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SocialPaymentsComponent } from './list/social-payments.component';
import { SocialPaymentsDetailComponent } from './detail/social-payments-detail.component';
import { SocialPaymentsUpdateComponent } from './update/social-payments-update.component';
import { SocialPaymentsDeleteDialogComponent } from './delete/social-payments-delete-dialog.component';
import { SocialPaymentsRoutingModule } from './route/social-payments-routing.module';

@NgModule({
  imports: [SharedModule, SocialPaymentsRoutingModule],
  declarations: [
    SocialPaymentsComponent,
    SocialPaymentsDetailComponent,
    SocialPaymentsUpdateComponent,
    SocialPaymentsDeleteDialogComponent,
  ],
  entryComponents: [SocialPaymentsDeleteDialogComponent],
})
export class SocialPaymentsModule {}
