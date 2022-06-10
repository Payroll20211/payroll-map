import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPeriod } from '../period.model';
import { PeriodService } from '../service/period.service';
import { PeriodDeleteDialogComponent } from '../delete/period-delete-dialog.component';

@Component({
  selector: 'payroll-period',
  templateUrl: './period.component.html',
})
export class PeriodComponent implements OnInit {
  periods?: IPeriod[];
  isLoading = false;

  constructor(protected periodService: PeriodService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.periodService.query().subscribe({
      next: (res: HttpResponse<IPeriod[]>) => {
        this.isLoading = false;
        this.periods = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPeriod): number {
    return item.id!;
  }

  delete(period: IPeriod): void {
    const modalRef = this.modalService.open(PeriodDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.period = period;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
