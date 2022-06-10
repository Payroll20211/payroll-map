import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOperatorType, getOperatorTypeIdentifier } from '../operator-type.model';

export type EntityResponseType = HttpResponse<IOperatorType>;
export type EntityArrayResponseType = HttpResponse<IOperatorType[]>;

@Injectable({ providedIn: 'root' })
export class OperatorTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/operator-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(operatorType: IOperatorType): Observable<EntityResponseType> {
    return this.http.post<IOperatorType>(this.resourceUrl, operatorType, { observe: 'response' });
  }

  update(operatorType: IOperatorType): Observable<EntityResponseType> {
    return this.http.put<IOperatorType>(`${this.resourceUrl}/${getOperatorTypeIdentifier(operatorType) as number}`, operatorType, {
      observe: 'response',
    });
  }

  partialUpdate(operatorType: IOperatorType): Observable<EntityResponseType> {
    return this.http.patch<IOperatorType>(`${this.resourceUrl}/${getOperatorTypeIdentifier(operatorType) as number}`, operatorType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOperatorType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOperatorType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOperatorTypeToCollectionIfMissing(
    operatorTypeCollection: IOperatorType[],
    ...operatorTypesToCheck: (IOperatorType | null | undefined)[]
  ): IOperatorType[] {
    const operatorTypes: IOperatorType[] = operatorTypesToCheck.filter(isPresent);
    if (operatorTypes.length > 0) {
      const operatorTypeCollectionIdentifiers = operatorTypeCollection.map(
        operatorTypeItem => getOperatorTypeIdentifier(operatorTypeItem)!
      );
      const operatorTypesToAdd = operatorTypes.filter(operatorTypeItem => {
        const operatorTypeIdentifier = getOperatorTypeIdentifier(operatorTypeItem);
        if (operatorTypeIdentifier == null || operatorTypeCollectionIdentifiers.includes(operatorTypeIdentifier)) {
          return false;
        }
        operatorTypeCollectionIdentifiers.push(operatorTypeIdentifier);
        return true;
      });
      return [...operatorTypesToAdd, ...operatorTypeCollection];
    }
    return operatorTypeCollection;
  }
}
