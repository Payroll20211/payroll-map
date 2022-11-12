import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPositionArl } from '../position-arl.model';
import { PositionArlService } from '../service/position-arl.service';
import { PositionArlDeleteDialogComponent } from '../delete/position-arl-delete-dialog.component';

@Component({
  selector: 'payroll-position-arl',
  templateUrl: './position-arl.component.html',
})
export class PositionArlComponent implements OnInit {
  positionArls?: IPositionArl[];
  isLoading = false;

  constructor(protected positionArlService: PositionArlService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.positionArlService.query().subscribe({
      next: (res: HttpResponse<IPositionArl[]>) => {
        this.isLoading = false;
        this.positionArls = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPositionArl): number {
    return item.id!;
  }

  delete(positionArl: IPositionArl): void {
    const modalRef = this.modalService.open(PositionArlDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.positionArl = positionArl;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
