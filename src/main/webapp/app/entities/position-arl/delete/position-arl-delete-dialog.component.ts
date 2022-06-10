import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPositionArl } from '../position-arl.model';
import { PositionArlService } from '../service/position-arl.service';

@Component({
  templateUrl: './position-arl-delete-dialog.component.html',
})
export class PositionArlDeleteDialogComponent {
  positionArl?: IPositionArl;

  constructor(protected positionArlService: PositionArlService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.positionArlService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
