import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContract, getContractIdentifier } from '../contract.model';

export type EntityResponseType = HttpResponse<IContract>;
export type EntityArrayResponseType = HttpResponse<IContract[]>;

@Injectable({ providedIn: 'root' })
export class ContractService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contracts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contract: IContract): Observable<EntityResponseType> {
    return this.http.post<IContract>(this.resourceUrl, contract, { observe: 'response' });
  }

  update(contract: IContract): Observable<EntityResponseType> {
    return this.http.put<IContract>(`${this.resourceUrl}/${getContractIdentifier(contract) as number}`, contract, { observe: 'response' });
  }

  partialUpdate(contract: IContract): Observable<EntityResponseType> {
    return this.http.patch<IContract>(`${this.resourceUrl}/${getContractIdentifier(contract) as number}`, contract, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContract>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContract[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addContractToCollectionIfMissing(contractCollection: IContract[], ...contractsToCheck: (IContract | null | undefined)[]): IContract[] {
    const contracts: IContract[] = contractsToCheck.filter(isPresent);
    if (contracts.length > 0) {
      const contractCollectionIdentifiers = contractCollection.map(contractItem => getContractIdentifier(contractItem)!);
      const contractsToAdd = contracts.filter(contractItem => {
        const contractIdentifier = getContractIdentifier(contractItem);
        if (contractIdentifier == null || contractCollectionIdentifiers.includes(contractIdentifier)) {
          return false;
        }
        contractCollectionIdentifiers.push(contractIdentifier);
        return true;
      });
      return [...contractsToAdd, ...contractCollection];
    }
    return contractCollection;
  }
}
