import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOperatorMatriz } from '../operator-matriz.model';
import { OperatorMatrizService } from '../service/operator-matriz.service';

@Component({
  templateUrl: './operator-matriz-delete-dialog.component.html',
})
export class OperatorMatrizDeleteDialogComponent {
  operatorMatriz?: IOperatorMatriz;

  constructor(protected operatorMatrizService: OperatorMatrizService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.operatorMatrizService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
