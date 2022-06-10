import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPeriod, Period } from '../period.model';
import { PeriodService } from '../service/period.service';

@Injectable({ providedIn: 'root' })
export class PeriodRoutingResolveService implements Resolve<IPeriod> {
  constructor(protected service: PeriodService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPeriod> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((period: HttpResponse<Period>) => {
          if (period.body) {
            return of(period.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Period());
  }
}
