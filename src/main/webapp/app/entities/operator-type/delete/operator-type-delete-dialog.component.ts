import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOperatorType } from '../operator-type.model';
import { OperatorTypeService } from '../service/operator-type.service';

@Component({
  templateUrl: './operator-type-delete-dialog.component.html',
})
export class OperatorTypeDeleteDialogComponent {
  operatorType?: IOperatorType;

  constructor(protected operatorTypeService: OperatorTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.operatorTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
