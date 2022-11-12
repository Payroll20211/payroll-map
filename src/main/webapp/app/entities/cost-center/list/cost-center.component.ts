import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICostCenter } from '../cost-center.model';
import { CostCenterService } from '../service/cost-center.service';
import { CostCenterDeleteDialogComponent } from '../delete/cost-center-delete-dialog.component';

@Component({
  selector: 'payroll-cost-center',
  templateUrl: './cost-center.component.html',
})
export class CostCenterComponent implements OnInit {
  costCenters?: ICostCenter[];
  isLoading = false;

  constructor(protected costCenterService: CostCenterService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.costCenterService.query().subscribe({
      next: (res: HttpResponse<ICostCenter[]>) => {
        this.isLoading = false;
        this.costCenters = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ICostCenter): number {
    return item.id!;
  }

  delete(costCenter: ICostCenter): void {
    const modalRef = this.modalService.open(CostCenterDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.costCenter = costCenter;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
