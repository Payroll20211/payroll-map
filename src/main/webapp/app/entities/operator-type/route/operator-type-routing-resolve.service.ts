import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOperatorType, OperatorType } from '../operator-type.model';
import { OperatorTypeService } from '../service/operator-type.service';

@Injectable({ providedIn: 'root' })
export class OperatorTypeRoutingResolveService implements Resolve<IOperatorType> {
  constructor(protected service: OperatorTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOperatorType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((operatorType: HttpResponse<OperatorType>) => {
          if (operatorType.body) {
            return of(operatorType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OperatorType());
  }
}
