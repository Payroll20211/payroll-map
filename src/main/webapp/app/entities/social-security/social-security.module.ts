import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SocialSecurityComponent } from './list/social-security.component';
import { SocialSecurityDetailComponent } from './detail/social-security-detail.component';
import { SocialSecurityUpdateComponent } from './update/social-security-update.component';
import { SocialSecurityDeleteDialogComponent } from './delete/social-security-delete-dialog.component';
import { SocialSecurityRoutingModule } from './route/social-security-routing.module';

@NgModule({
  imports: [SharedModule, SocialSecurityRoutingModule],
  declarations: [
    SocialSecurityComponent,
    SocialSecurityDetailComponent,
    SocialSecurityUpdateComponent,
    SocialSecurityDeleteDialogComponent,
  ],
  entryComponents: [SocialSecurityDeleteDialogComponent],
})
export class SocialSecurityModule {}
