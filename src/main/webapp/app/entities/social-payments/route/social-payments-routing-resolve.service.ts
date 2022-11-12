import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISocialPayments, SocialPayments } from '../social-payments.model';
import { SocialPaymentsService } from '../service/social-payments.service';

@Injectable({ providedIn: 'root' })
export class SocialPaymentsRoutingResolveService implements Resolve<ISocialPayments> {
  constructor(protected service: SocialPaymentsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISocialPayments> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((socialPayments: HttpResponse<SocialPayments>) => {
          if (socialPayments.body) {
            return of(socialPayments.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SocialPayments());
  }
}
