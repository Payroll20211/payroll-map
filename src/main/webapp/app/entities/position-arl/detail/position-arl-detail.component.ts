import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPositionArl } from '../position-arl.model';

@Component({
  selector: 'payroll-position-arl-detail',
  templateUrl: './position-arl-detail.component.html',
})
export class PositionArlDetailComponent implements OnInit {
  positionArl: IPositionArl | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ positionArl }) => {
      this.positionArl = positionArl;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
