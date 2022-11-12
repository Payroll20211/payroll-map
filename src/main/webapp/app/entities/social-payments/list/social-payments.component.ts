import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISocialPayments } from '../social-payments.model';
import { SocialPaymentsService } from '../service/social-payments.service';
import { SocialPaymentsDeleteDialogComponent } from '../delete/social-payments-delete-dialog.component';

@Component({
  selector: 'payroll-social-payments',
  templateUrl: './social-payments.component.html',
})
export class SocialPaymentsComponent implements OnInit {
  socialPayments?: ISocialPayments[];
  isLoading = false;

  constructor(protected socialPaymentsService: SocialPaymentsService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.socialPaymentsService.query().subscribe({
      next: (res: HttpResponse<ISocialPayments[]>) => {
        this.isLoading = false;
        this.socialPayments = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ISocialPayments): number {
    return item.id!;
  }

  delete(socialPayments: ISocialPayments): void {
    const modalRef = this.modalService.open(SocialPaymentsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.socialPayments = socialPayments;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
