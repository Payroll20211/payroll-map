import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProjectMaster, ProjectMaster } from '../project-master.model';
import { ProjectMasterService } from '../service/project-master.service';

@Injectable({ providedIn: 'root' })
export class ProjectMasterRoutingResolveService implements Resolve<IProjectMaster> {
  constructor(protected service: ProjectMasterService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProjectMaster> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((projectMaster: HttpResponse<ProjectMaster>) => {
          if (projectMaster.body) {
            return of(projectMaster.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ProjectMaster());
  }
}
