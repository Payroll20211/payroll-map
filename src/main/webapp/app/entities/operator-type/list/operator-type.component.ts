import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOperatorType } from '../operator-type.model';
import { OperatorTypeService } from '../service/operator-type.service';
import { OperatorTypeDeleteDialogComponent } from '../delete/operator-type-delete-dialog.component';

@Component({
  selector: 'payroll-operator-type',
  templateUrl: './operator-type.component.html',
})
export class OperatorTypeComponent implements OnInit {
  operatorTypes?: IOperatorType[];
  isLoading = false;

  constructor(protected operatorTypeService: OperatorTypeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.operatorTypeService.query().subscribe({
      next: (res: HttpResponse<IOperatorType[]>) => {
        this.isLoading = false;
        this.operatorTypes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IOperatorType): number {
    return item.id!;
  }

  delete(operatorType: IOperatorType): void {
    const modalRef = this.modalService.open(OperatorTypeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.operatorType = operatorType;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
