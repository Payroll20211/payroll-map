import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPositionArl, PositionArl } from '../position-arl.model';
import { PositionArlService } from '../service/position-arl.service';

@Injectable({ providedIn: 'root' })
export class PositionArlRoutingResolveService implements Resolve<IPositionArl> {
  constructor(protected service: PositionArlService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPositionArl> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((positionArl: HttpResponse<PositionArl>) => {
          if (positionArl.body) {
            return of(positionArl.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PositionArl());
  }
}
