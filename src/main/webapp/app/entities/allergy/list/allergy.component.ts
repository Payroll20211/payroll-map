import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAllergy } from '../allergy.model';
import { AllergyService } from '../service/allergy.service';
import { AllergyDeleteDialogComponent } from '../delete/allergy-delete-dialog.component';

@Component({
  selector: 'payroll-allergy',
  templateUrl: './allergy.component.html',
})
export class AllergyComponent implements OnInit {
  allergies?: IAllergy[];
  isLoading = false;

  constructor(protected allergyService: AllergyService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.allergyService.query().subscribe({
      next: (res: HttpResponse<IAllergy[]>) => {
        this.isLoading = false;
        this.allergies = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IAllergy): number {
    return item.id!;
  }

  delete(allergy: IAllergy): void {
    const modalRef = this.modalService.open(AllergyDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.allergy = allergy;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
