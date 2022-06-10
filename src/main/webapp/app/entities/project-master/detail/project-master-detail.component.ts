import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProjectMaster } from '../project-master.model';

@Component({
  selector: 'payroll-project-master-detail',
  templateUrl: './project-master-detail.component.html',
})
export class ProjectMasterDetailComponent implements OnInit {
  projectMaster: IProjectMaster | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ projectMaster }) => {
      this.projectMaster = projectMaster;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
