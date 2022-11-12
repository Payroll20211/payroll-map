import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICostCenter } from '../cost-center.model';

@Component({
  selector: 'payroll-cost-center-detail',
  templateUrl: './cost-center-detail.component.html',
})
export class CostCenterDetailComponent implements OnInit {
  costCenter: ICostCenter | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ costCenter }) => {
      this.costCenter = costCenter;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
