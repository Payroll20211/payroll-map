import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISocialSecurity } from '../social-security.model';
import { SocialSecurityService } from '../service/social-security.service';

@Component({
  templateUrl: './social-security-delete-dialog.component.html',
})
export class SocialSecurityDeleteDialogComponent {
  socialSecurity?: ISocialSecurity;

  constructor(protected socialSecurityService: SocialSecurityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.socialSecurityService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
