import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IContract } from '../contract.model';
import { ContractService } from '../service/contract.service';
import { ContractDeleteDialogComponent } from '../delete/contract-delete-dialog.component';

@Component({
  selector: 'payroll-contract',
  templateUrl: './contract.component.html',
})
export class ContractComponent implements OnInit {
  contracts?: IContract[];
  isLoading = false;

  constructor(protected contractService: ContractService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.contractService.query().subscribe({
      next: (res: HttpResponse<IContract[]>) => {
        this.isLoading = false;
        this.contracts = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IContract): number {
    return item.id!;
  }

  delete(contract: IContract): void {
    const modalRef = this.modalService.open(ContractDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.contract = contract;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
