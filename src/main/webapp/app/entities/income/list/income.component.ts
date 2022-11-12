import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IIncome } from '../income.model';
import { IncomeService } from '../service/income.service';
import { IncomeDeleteDialogComponent } from '../delete/income-delete-dialog.component';

@Component({
  selector: 'payroll-income',
  templateUrl: './income.component.html',
})
export class IncomeComponent implements OnInit {
  incomes?: IIncome[];
  isLoading = false;

  constructor(protected incomeService: IncomeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.incomeService.query().subscribe({
      next: (res: HttpResponse<IIncome[]>) => {
        this.isLoading = false;
        this.incomes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IIncome): number {
    return item.id!;
  }

  delete(income: IIncome): void {
    const modalRef = this.modalService.open(IncomeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.income = income;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
