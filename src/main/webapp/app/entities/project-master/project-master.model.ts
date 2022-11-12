import { ICostCenter } from 'app/entities/cost-center/cost-center.model';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface IProjectMaster {
  id?: number;
  projectMasterCode?: string;
  projectMasterName?: string;
  costCenterType?: string;
  projectDirectorName?: string;
  phone?: string;
  costCenter?: ICostCenter;
  employee?: IEmployee | null;
}

export class ProjectMaster implements IProjectMaster {
  constructor(
    public id?: number,
    public projectMasterCode?: string,
    public projectMasterName?: string,
    public costCenterType?: string,
    public projectDirectorName?: string,
    public phone?: string,
    public costCenter?: ICostCenter,
    public employee?: IEmployee | null
  ) {}
}

export function getProjectMasterIdentifier(projectMaster: IProjectMaster): number | undefined {
  return projectMaster.id;
}
