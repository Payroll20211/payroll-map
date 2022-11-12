import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOperatorMatriz } from '../operator-matriz.model';

@Component({
  selector: 'payroll-operator-matriz-detail',
  templateUrl: './operator-matriz-detail.component.html',
})
export class OperatorMatrizDetailComponent implements OnInit {
  operatorMatriz: IOperatorMatriz | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ operatorMatriz }) => {
      this.operatorMatriz = operatorMatriz;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
