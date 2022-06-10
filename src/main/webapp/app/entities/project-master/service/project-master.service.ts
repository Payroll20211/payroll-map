import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProjectMaster, getProjectMasterIdentifier } from '../project-master.model';

export type EntityResponseType = HttpResponse<IProjectMaster>;
export type EntityArrayResponseType = HttpResponse<IProjectMaster[]>;

@Injectable({ providedIn: 'root' })
export class ProjectMasterService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/project-masters');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(projectMaster: IProjectMaster): Observable<EntityResponseType> {
    return this.http.post<IProjectMaster>(this.resourceUrl, projectMaster, { observe: 'response' });
  }

  update(projectMaster: IProjectMaster): Observable<EntityResponseType> {
    return this.http.put<IProjectMaster>(`${this.resourceUrl}/${getProjectMasterIdentifier(projectMaster) as number}`, projectMaster, {
      observe: 'response',
    });
  }

  partialUpdate(projectMaster: IProjectMaster): Observable<EntityResponseType> {
    return this.http.patch<IProjectMaster>(`${this.resourceUrl}/${getProjectMasterIdentifier(projectMaster) as number}`, projectMaster, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProjectMaster>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProjectMaster[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProjectMasterToCollectionIfMissing(
    projectMasterCollection: IProjectMaster[],
    ...projectMastersToCheck: (IProjectMaster | null | undefined)[]
  ): IProjectMaster[] {
    const projectMasters: IProjectMaster[] = projectMastersToCheck.filter(isPresent);
    if (projectMasters.length > 0) {
      const projectMasterCollectionIdentifiers = projectMasterCollection.map(
        projectMasterItem => getProjectMasterIdentifier(projectMasterItem)!
      );
      const projectMastersToAdd = projectMasters.filter(projectMasterItem => {
        const projectMasterIdentifier = getProjectMasterIdentifier(projectMasterItem);
        if (projectMasterIdentifier == null || projectMasterCollectionIdentifiers.includes(projectMasterIdentifier)) {
          return false;
        }
        projectMasterCollectionIdentifiers.push(projectMasterIdentifier);
        return true;
      });
      return [...projectMastersToAdd, ...projectMasterCollection];
    }
    return projectMasterCollection;
  }
}
