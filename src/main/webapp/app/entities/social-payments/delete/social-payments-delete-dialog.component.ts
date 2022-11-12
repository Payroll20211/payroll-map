import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISocialPayments } from '../social-payments.model';
import { SocialPaymentsService } from '../service/social-payments.service';

@Component({
  templateUrl: './social-payments-delete-dialog.component.html',
})
export class SocialPaymentsDeleteDialogComponent {
  socialPayments?: ISocialPayments;

  constructor(protected socialPaymentsService: SocialPaymentsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.socialPaymentsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
