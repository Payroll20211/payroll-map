import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAllergy, getAllergyIdentifier } from '../allergy.model';

export type EntityResponseType = HttpResponse<IAllergy>;
export type EntityArrayResponseType = HttpResponse<IAllergy[]>;

@Injectable({ providedIn: 'root' })
export class AllergyService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/allergies');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(allergy: IAllergy): Observable<EntityResponseType> {
    return this.http.post<IAllergy>(this.resourceUrl, allergy, { observe: 'response' });
  }

  update(allergy: IAllergy): Observable<EntityResponseType> {
    return this.http.put<IAllergy>(`${this.resourceUrl}/${getAllergyIdentifier(allergy) as number}`, allergy, { observe: 'response' });
  }

  partialUpdate(allergy: IAllergy): Observable<EntityResponseType> {
    return this.http.patch<IAllergy>(`${this.resourceUrl}/${getAllergyIdentifier(allergy) as number}`, allergy, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAllergy>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAllergy[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAllergyToCollectionIfMissing(allergyCollection: IAllergy[], ...allergiesToCheck: (IAllergy | null | undefined)[]): IAllergy[] {
    const allergies: IAllergy[] = allergiesToCheck.filter(isPresent);
    if (allergies.length > 0) {
      const allergyCollectionIdentifiers = allergyCollection.map(allergyItem => getAllergyIdentifier(allergyItem)!);
      const allergiesToAdd = allergies.filter(allergyItem => {
        const allergyIdentifier = getAllergyIdentifier(allergyItem);
        if (allergyIdentifier == null || allergyCollectionIdentifiers.includes(allergyIdentifier)) {
          return false;
        }
        allergyCollectionIdentifiers.push(allergyIdentifier);
        return true;
      });
      return [...allergiesToAdd, ...allergyCollection];
    }
    return allergyCollection;
  }
}
