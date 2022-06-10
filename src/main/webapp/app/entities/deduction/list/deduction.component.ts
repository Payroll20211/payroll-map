import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeduction } from '../deduction.model';
import { DeductionService } from '../service/deduction.service';
import { DeductionDeleteDialogComponent } from '../delete/deduction-delete-dialog.component';

@Component({
  selector: 'payroll-deduction',
  templateUrl: './deduction.component.html',
})
export class DeductionComponent implements OnInit {
  deductions?: IDeduction[];
  isLoading = false;

  constructor(protected deductionService: DeductionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.deductionService.query().subscribe({
      next: (res: HttpResponse<IDeduction[]>) => {
        this.isLoading = false;
        this.deductions = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IDeduction): number {
    return item.id!;
  }

  delete(deduction: IDeduction): void {
    const modalRef = this.modalService.open(DeductionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.deduction = deduction;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
