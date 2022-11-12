import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProjectMaster } from '../project-master.model';
import { ProjectMasterService } from '../service/project-master.service';

@Component({
  templateUrl: './project-master-delete-dialog.component.html',
})
export class ProjectMasterDeleteDialogComponent {
  projectMaster?: IProjectMaster;

  constructor(protected projectMasterService: ProjectMasterService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.projectMasterService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
