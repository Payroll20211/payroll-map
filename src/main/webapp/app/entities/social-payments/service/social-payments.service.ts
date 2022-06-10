import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISocialPayments, getSocialPaymentsIdentifier } from '../social-payments.model';

export type EntityResponseType = HttpResponse<ISocialPayments>;
export type EntityArrayResponseType = HttpResponse<ISocialPayments[]>;

@Injectable({ providedIn: 'root' })
export class SocialPaymentsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/social-payments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(socialPayments: ISocialPayments): Observable<EntityResponseType> {
    return this.http.post<ISocialPayments>(this.resourceUrl, socialPayments, { observe: 'response' });
  }

  update(socialPayments: ISocialPayments): Observable<EntityResponseType> {
    return this.http.put<ISocialPayments>(`${this.resourceUrl}/${getSocialPaymentsIdentifier(socialPayments) as number}`, socialPayments, {
      observe: 'response',
    });
  }

  partialUpdate(socialPayments: ISocialPayments): Observable<EntityResponseType> {
    return this.http.patch<ISocialPayments>(
      `${this.resourceUrl}/${getSocialPaymentsIdentifier(socialPayments) as number}`,
      socialPayments,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISocialPayments>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISocialPayments[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSocialPaymentsToCollectionIfMissing(
    socialPaymentsCollection: ISocialPayments[],
    ...socialPaymentsToCheck: (ISocialPayments | null | undefined)[]
  ): ISocialPayments[] {
    const socialPayments: ISocialPayments[] = socialPaymentsToCheck.filter(isPresent);
    if (socialPayments.length > 0) {
      const socialPaymentsCollectionIdentifiers = socialPaymentsCollection.map(
        socialPaymentsItem => getSocialPaymentsIdentifier(socialPaymentsItem)!
      );
      const socialPaymentsToAdd = socialPayments.filter(socialPaymentsItem => {
        const socialPaymentsIdentifier = getSocialPaymentsIdentifier(socialPaymentsItem);
        if (socialPaymentsIdentifier == null || socialPaymentsCollectionIdentifiers.includes(socialPaymentsIdentifier)) {
          return false;
        }
        socialPaymentsCollectionIdentifiers.push(socialPaymentsIdentifier);
        return true;
      });
      return [...socialPaymentsToAdd, ...socialPaymentsCollection];
    }
    return socialPaymentsCollection;
  }
}
