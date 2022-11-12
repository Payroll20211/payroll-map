import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOperatorMatriz, getOperatorMatrizIdentifier } from '../operator-matriz.model';

export type EntityResponseType = HttpResponse<IOperatorMatriz>;
export type EntityArrayResponseType = HttpResponse<IOperatorMatriz[]>;

@Injectable({ providedIn: 'root' })
export class OperatorMatrizService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/operator-matrizs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(operatorMatriz: IOperatorMatriz): Observable<EntityResponseType> {
    return this.http.post<IOperatorMatriz>(this.resourceUrl, operatorMatriz, { observe: 'response' });
  }

  update(operatorMatriz: IOperatorMatriz): Observable<EntityResponseType> {
    return this.http.put<IOperatorMatriz>(`${this.resourceUrl}/${getOperatorMatrizIdentifier(operatorMatriz) as number}`, operatorMatriz, {
      observe: 'response',
    });
  }

  partialUpdate(operatorMatriz: IOperatorMatriz): Observable<EntityResponseType> {
    return this.http.patch<IOperatorMatriz>(
      `${this.resourceUrl}/${getOperatorMatrizIdentifier(operatorMatriz) as number}`,
      operatorMatriz,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOperatorMatriz>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOperatorMatriz[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOperatorMatrizToCollectionIfMissing(
    operatorMatrizCollection: IOperatorMatriz[],
    ...operatorMatrizsToCheck: (IOperatorMatriz | null | undefined)[]
  ): IOperatorMatriz[] {
    const operatorMatrizs: IOperatorMatriz[] = operatorMatrizsToCheck.filter(isPresent);
    if (operatorMatrizs.length > 0) {
      const operatorMatrizCollectionIdentifiers = operatorMatrizCollection.map(
        operatorMatrizItem => getOperatorMatrizIdentifier(operatorMatrizItem)!
      );
      const operatorMatrizsToAdd = operatorMatrizs.filter(operatorMatrizItem => {
        const operatorMatrizIdentifier = getOperatorMatrizIdentifier(operatorMatrizItem);
        if (operatorMatrizIdentifier == null || operatorMatrizCollectionIdentifiers.includes(operatorMatrizIdentifier)) {
          return false;
        }
        operatorMatrizCollectionIdentifiers.push(operatorMatrizIdentifier);
        return true;
      });
      return [...operatorMatrizsToAdd, ...operatorMatrizCollection];
    }
    return operatorMatrizCollection;
  }
}
