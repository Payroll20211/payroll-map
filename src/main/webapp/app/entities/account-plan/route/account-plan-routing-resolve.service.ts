import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAccountPlan, AccountPlan } from '../account-plan.model';
import { AccountPlanService } from '../service/account-plan.service';

@Injectable({ providedIn: 'root' })
export class AccountPlanRoutingResolveService implements Resolve<IAccountPlan> {
  constructor(protected service: AccountPlanService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAccountPlan> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((accountPlan: HttpResponse<AccountPlan>) => {
          if (accountPlan.body) {
            return of(accountPlan.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AccountPlan());
  }
}
