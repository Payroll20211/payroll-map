import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISocialPayments } from '../social-payments.model';

@Component({
  selector: 'payroll-social-payments-detail',
  templateUrl: './social-payments-detail.component.html',
})
export class SocialPaymentsDetailComponent implements OnInit {
  socialPayments: ISocialPayments | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ socialPayments }) => {
      this.socialPayments = socialPayments;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
