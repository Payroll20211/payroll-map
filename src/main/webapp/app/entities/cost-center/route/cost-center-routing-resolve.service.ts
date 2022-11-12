import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICostCenter, CostCenter } from '../cost-center.model';
import { CostCenterService } from '../service/cost-center.service';

@Injectable({ providedIn: 'root' })
export class CostCenterRoutingResolveService implements Resolve<ICostCenter> {
  constructor(protected service: CostCenterService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICostCenter> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((costCenter: HttpResponse<CostCenter>) => {
          if (costCenter.body) {
            return of(costCenter.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CostCenter());
  }
}
