import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISocialSecurity } from '../social-security.model';
import { SocialSecurityService } from '../service/social-security.service';
import { SocialSecurityDeleteDialogComponent } from '../delete/social-security-delete-dialog.component';

@Component({
  selector: 'payroll-social-security',
  templateUrl: './social-security.component.html',
})
export class SocialSecurityComponent implements OnInit {
  socialSecurities?: ISocialSecurity[];
  isLoading = false;

  constructor(protected socialSecurityService: SocialSecurityService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.socialSecurityService.query().subscribe({
      next: (res: HttpResponse<ISocialSecurity[]>) => {
        this.isLoading = false;
        this.socialSecurities = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ISocialSecurity): number {
    return item.id!;
  }

  delete(socialSecurity: ISocialSecurity): void {
    const modalRef = this.modalService.open(SocialSecurityDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.socialSecurity = socialSecurity;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
