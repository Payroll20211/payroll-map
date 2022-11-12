import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPositionArl, getPositionArlIdentifier } from '../position-arl.model';

export type EntityResponseType = HttpResponse<IPositionArl>;
export type EntityArrayResponseType = HttpResponse<IPositionArl[]>;

@Injectable({ providedIn: 'root' })
export class PositionArlService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/position-arls');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(positionArl: IPositionArl): Observable<EntityResponseType> {
    return this.http.post<IPositionArl>(this.resourceUrl, positionArl, { observe: 'response' });
  }

  update(positionArl: IPositionArl): Observable<EntityResponseType> {
    return this.http.put<IPositionArl>(`${this.resourceUrl}/${getPositionArlIdentifier(positionArl) as number}`, positionArl, {
      observe: 'response',
    });
  }

  partialUpdate(positionArl: IPositionArl): Observable<EntityResponseType> {
    return this.http.patch<IPositionArl>(`${this.resourceUrl}/${getPositionArlIdentifier(positionArl) as number}`, positionArl, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPositionArl>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPositionArl[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPositionArlToCollectionIfMissing(
    positionArlCollection: IPositionArl[],
    ...positionArlsToCheck: (IPositionArl | null | undefined)[]
  ): IPositionArl[] {
    const positionArls: IPositionArl[] = positionArlsToCheck.filter(isPresent);
    if (positionArls.length > 0) {
      const positionArlCollectionIdentifiers = positionArlCollection.map(positionArlItem => getPositionArlIdentifier(positionArlItem)!);
      const positionArlsToAdd = positionArls.filter(positionArlItem => {
        const positionArlIdentifier = getPositionArlIdentifier(positionArlItem);
        if (positionArlIdentifier == null || positionArlCollectionIdentifiers.includes(positionArlIdentifier)) {
          return false;
        }
        positionArlCollectionIdentifiers.push(positionArlIdentifier);
        return true;
      });
      return [...positionArlsToAdd, ...positionArlCollection];
    }
    return positionArlCollection;
  }
}
