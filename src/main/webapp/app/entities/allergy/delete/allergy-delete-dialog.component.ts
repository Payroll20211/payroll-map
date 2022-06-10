import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAllergy } from '../allergy.model';
import { AllergyService } from '../service/allergy.service';

@Component({
  templateUrl: './allergy-delete-dialog.component.html',
})
export class AllergyDeleteDialogComponent {
  allergy?: IAllergy;

  constructor(protected allergyService: AllergyService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.allergyService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
