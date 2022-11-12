import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISocialSecurity, getSocialSecurityIdentifier } from '../social-security.model';

export type EntityResponseType = HttpResponse<ISocialSecurity>;
export type EntityArrayResponseType = HttpResponse<ISocialSecurity[]>;

@Injectable({ providedIn: 'root' })
export class SocialSecurityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/social-securities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(socialSecurity: ISocialSecurity): Observable<EntityResponseType> {
    return this.http.post<ISocialSecurity>(this.resourceUrl, socialSecurity, { observe: 'response' });
  }

  update(socialSecurity: ISocialSecurity): Observable<EntityResponseType> {
    return this.http.put<ISocialSecurity>(`${this.resourceUrl}/${getSocialSecurityIdentifier(socialSecurity) as number}`, socialSecurity, {
      observe: 'response',
    });
  }

  partialUpdate(socialSecurity: ISocialSecurity): Observable<EntityResponseType> {
    return this.http.patch<ISocialSecurity>(
      `${this.resourceUrl}/${getSocialSecurityIdentifier(socialSecurity) as number}`,
      socialSecurity,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISocialSecurity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISocialSecurity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSocialSecurityToCollectionIfMissing(
    socialSecurityCollection: ISocialSecurity[],
    ...socialSecuritiesToCheck: (ISocialSecurity | null | undefined)[]
  ): ISocialSecurity[] {
    const socialSecurities: ISocialSecurity[] = socialSecuritiesToCheck.filter(isPresent);
    if (socialSecurities.length > 0) {
      const socialSecurityCollectionIdentifiers = socialSecurityCollection.map(
        socialSecurityItem => getSocialSecurityIdentifier(socialSecurityItem)!
      );
      const socialSecuritiesToAdd = socialSecurities.filter(socialSecurityItem => {
        const socialSecurityIdentifier = getSocialSecurityIdentifier(socialSecurityItem);
        if (socialSecurityIdentifier == null || socialSecurityCollectionIdentifiers.includes(socialSecurityIdentifier)) {
          return false;
        }
        socialSecurityCollectionIdentifiers.push(socialSecurityIdentifier);
        return true;
      });
      return [...socialSecuritiesToAdd, ...socialSecurityCollection];
    }
    return socialSecurityCollection;
  }
}
