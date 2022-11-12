import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAccountPlan, getAccountPlanIdentifier } from '../account-plan.model';

export type EntityResponseType = HttpResponse<IAccountPlan>;
export type EntityArrayResponseType = HttpResponse<IAccountPlan[]>;

@Injectable({ providedIn: 'root' })
export class AccountPlanService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/account-plans');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(accountPlan: IAccountPlan): Observable<EntityResponseType> {
    return this.http.post<IAccountPlan>(this.resourceUrl, accountPlan, { observe: 'response' });
  }

  update(accountPlan: IAccountPlan): Observable<EntityResponseType> {
    return this.http.put<IAccountPlan>(`${this.resourceUrl}/${getAccountPlanIdentifier(accountPlan) as number}`, accountPlan, {
      observe: 'response',
    });
  }

  partialUpdate(accountPlan: IAccountPlan): Observable<EntityResponseType> {
    return this.http.patch<IAccountPlan>(`${this.resourceUrl}/${getAccountPlanIdentifier(accountPlan) as number}`, accountPlan, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAccountPlan>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAccountPlan[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAccountPlanToCollectionIfMissing(
    accountPlanCollection: IAccountPlan[],
    ...accountPlansToCheck: (IAccountPlan | null | undefined)[]
  ): IAccountPlan[] {
    const accountPlans: IAccountPlan[] = accountPlansToCheck.filter(isPresent);
    if (accountPlans.length > 0) {
      const accountPlanCollectionIdentifiers = accountPlanCollection.map(accountPlanItem => getAccountPlanIdentifier(accountPlanItem)!);
      const accountPlansToAdd = accountPlans.filter(accountPlanItem => {
        const accountPlanIdentifier = getAccountPlanIdentifier(accountPlanItem);
        if (accountPlanIdentifier == null || accountPlanCollectionIdentifiers.includes(accountPlanIdentifier)) {
          return false;
        }
        accountPlanCollectionIdentifiers.push(accountPlanIdentifier);
        return true;
      });
      return [...accountPlansToAdd, ...accountPlanCollection];
    }
    return accountPlanCollection;
  }
}
