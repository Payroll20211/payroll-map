import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProjectMaster } from '../project-master.model';
import { ProjectMasterService } from '../service/project-master.service';
import { ProjectMasterDeleteDialogComponent } from '../delete/project-master-delete-dialog.component';

@Component({
  selector: 'payroll-project-master',
  templateUrl: './project-master.component.html',
})
export class ProjectMasterComponent implements OnInit {
  projectMasters?: IProjectMaster[];
  isLoading = false;

  constructor(protected projectMasterService: ProjectMasterService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.projectMasterService.query().subscribe({
      next: (res: HttpResponse<IProjectMaster[]>) => {
        this.isLoading = false;
        this.projectMasters = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IProjectMaster): number {
    return item.id!;
  }

  delete(projectMaster: IProjectMaster): void {
    const modalRef = this.modalService.open(ProjectMasterDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.projectMaster = projectMaster;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
