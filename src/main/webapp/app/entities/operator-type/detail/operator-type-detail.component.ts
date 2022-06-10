import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOperatorType } from '../operator-type.model';

@Component({
  selector: 'payroll-operator-type-detail',
  templateUrl: './operator-type-detail.component.html',
})
export class OperatorTypeDetailComponent implements OnInit {
  operatorType: IOperatorType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ operatorType }) => {
      this.operatorType = operatorType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
