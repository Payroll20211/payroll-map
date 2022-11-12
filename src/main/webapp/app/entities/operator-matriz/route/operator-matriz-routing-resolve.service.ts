import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOperatorMatriz, OperatorMatriz } from '../operator-matriz.model';
import { OperatorMatrizService } from '../service/operator-matriz.service';

@Injectable({ providedIn: 'root' })
export class OperatorMatrizRoutingResolveService implements Resolve<IOperatorMatriz> {
  constructor(protected service: OperatorMatrizService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOperatorMatriz> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((operatorMatriz: HttpResponse<OperatorMatriz>) => {
          if (operatorMatriz.body) {
            return of(operatorMatriz.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OperatorMatriz());
  }
}
