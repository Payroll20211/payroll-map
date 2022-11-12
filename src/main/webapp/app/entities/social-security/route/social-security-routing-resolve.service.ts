import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISocialSecurity, SocialSecurity } from '../social-security.model';
import { SocialSecurityService } from '../service/social-security.service';

@Injectable({ providedIn: 'root' })
export class SocialSecurityRoutingResolveService implements Resolve<ISocialSecurity> {
  constructor(protected service: SocialSecurityService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISocialSecurity> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((socialSecurity: HttpResponse<SocialSecurity>) => {
          if (socialSecurity.body) {
            return of(socialSecurity.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SocialSecurity());
  }
}
