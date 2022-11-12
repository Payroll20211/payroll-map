import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPeriod, getPeriodIdentifier } from '../period.model';

export type EntityResponseType = HttpResponse<IPeriod>;
export type EntityArrayResponseType = HttpResponse<IPeriod[]>;

@Injectable({ providedIn: 'root' })
export class PeriodService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/periods');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(period: IPeriod): Observable<EntityResponseType> {
    return this.http.post<IPeriod>(this.resourceUrl, period, { observe: 'response' });
  }

  update(period: IPeriod): Observable<EntityResponseType> {
    return this.http.put<IPeriod>(`${this.resourceUrl}/${getPeriodIdentifier(period) as number}`, period, { observe: 'response' });
  }

  partialUpdate(period: IPeriod): Observable<EntityResponseType> {
    return this.http.patch<IPeriod>(`${this.resourceUrl}/${getPeriodIdentifier(period) as number}`, period, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPeriod>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPeriod[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPeriodToCollectionIfMissing(periodCollection: IPeriod[], ...periodsToCheck: (IPeriod | null | undefined)[]): IPeriod[] {
    const periods: IPeriod[] = periodsToCheck.filter(isPresent);
    if (periods.length > 0) {
      const periodCollectionIdentifiers = periodCollection.map(periodItem => getPeriodIdentifier(periodItem)!);
      const periodsToAdd = periods.filter(periodItem => {
        const periodIdentifier = getPeriodIdentifier(periodItem);
        if (periodIdentifier == null || periodCollectionIdentifiers.includes(periodIdentifier)) {
          return false;
        }
        periodCollectionIdentifiers.push(periodIdentifier);
        return true;
      });
      return [...periodsToAdd, ...periodCollection];
    }
    return periodCollection;
  }
}
