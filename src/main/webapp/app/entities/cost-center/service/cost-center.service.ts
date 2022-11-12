import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICostCenter, getCostCenterIdentifier } from '../cost-center.model';

export type EntityResponseType = HttpResponse<ICostCenter>;
export type EntityArrayResponseType = HttpResponse<ICostCenter[]>;

@Injectable({ providedIn: 'root' })
export class CostCenterService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cost-centers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(costCenter: ICostCenter): Observable<EntityResponseType> {
    return this.http.post<ICostCenter>(this.resourceUrl, costCenter, { observe: 'response' });
  }

  update(costCenter: ICostCenter): Observable<EntityResponseType> {
    return this.http.put<ICostCenter>(`${this.resourceUrl}/${getCostCenterIdentifier(costCenter) as number}`, costCenter, {
      observe: 'response',
    });
  }

  partialUpdate(costCenter: ICostCenter): Observable<EntityResponseType> {
    return this.http.patch<ICostCenter>(`${this.resourceUrl}/${getCostCenterIdentifier(costCenter) as number}`, costCenter, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICostCenter>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICostCenter[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCostCenterToCollectionIfMissing(
    costCenterCollection: ICostCenter[],
    ...costCentersToCheck: (ICostCenter | null | undefined)[]
  ): ICostCenter[] {
    const costCenters: ICostCenter[] = costCentersToCheck.filter(isPresent);
    if (costCenters.length > 0) {
      const costCenterCollectionIdentifiers = costCenterCollection.map(costCenterItem => getCostCenterIdentifier(costCenterItem)!);
      const costCentersToAdd = costCenters.filter(costCenterItem => {
        const costCenterIdentifier = getCostCenterIdentifier(costCenterItem);
        if (costCenterIdentifier == null || costCenterCollectionIdentifiers.includes(costCenterIdentifier)) {
          return false;
        }
        costCenterCollectionIdentifiers.push(costCenterIdentifier);
        return true;
      });
      return [...costCentersToAdd, ...costCenterCollection];
    }
    return costCenterCollection;
  }
}
