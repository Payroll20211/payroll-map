import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAllergy } from '../allergy.model';

@Component({
  selector: 'payroll-allergy-detail',
  templateUrl: './allergy-detail.component.html',
})
export class AllergyDetailComponent implements OnInit {
  allergy: IAllergy | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ allergy }) => {
      this.allergy = allergy;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
