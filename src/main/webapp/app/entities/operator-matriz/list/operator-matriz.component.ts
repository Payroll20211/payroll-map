import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOperatorMatriz } from '../operator-matriz.model';
import { OperatorMatrizService } from '../service/operator-matriz.service';
import { OperatorMatrizDeleteDialogComponent } from '../delete/operator-matriz-delete-dialog.component';

@Component({
  selector: 'payroll-operator-matriz',
  templateUrl: './operator-matriz.component.html',
})
export class OperatorMatrizComponent implements OnInit {
  operatorMatrizs?: IOperatorMatriz[];
  isLoading = false;

  constructor(protected operatorMatrizService: OperatorMatrizService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.operatorMatrizService.query().subscribe({
      next: (res: HttpResponse<IOperatorMatriz[]>) => {
        this.isLoading = false;
        this.operatorMatrizs = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IOperatorMatriz): number {
    return item.id!;
  }

  delete(operatorMatriz: IOperatorMatriz): void {
    const modalRef = this.modalService.open(OperatorMatrizDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.operatorMatriz = operatorMatriz;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
