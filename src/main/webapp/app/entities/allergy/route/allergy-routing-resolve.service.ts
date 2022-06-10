import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAllergy, Allergy } from '../allergy.model';
import { AllergyService } from '../service/allergy.service';

@Injectable({ providedIn: 'root' })
export class AllergyRoutingResolveService implements Resolve<IAllergy> {
  constructor(protected service: AllergyService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAllergy> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((allergy: HttpResponse<Allergy>) => {
          if (allergy.body) {
            return of(allergy.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Allergy());
  }
}
