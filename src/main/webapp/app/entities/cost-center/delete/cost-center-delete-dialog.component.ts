import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICostCenter } from '../cost-center.model';
import { CostCenterService } from '../service/cost-center.service';

@Component({
  templateUrl: './cost-center-delete-dialog.component.html',
})
export class CostCenterDeleteDialogComponent {
  costCenter?: ICostCenter;

  constructor(protected costCenterService: CostCenterService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.costCenterService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
