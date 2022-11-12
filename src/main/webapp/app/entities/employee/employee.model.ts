import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IContract } from 'app/entities/contract/contract.model';
import { IAllergy } from 'app/entities/allergy/allergy.model';
import { ISocialPayments } from 'app/entities/social-payments/social-payments.model';
import { IPositionArl } from 'app/entities/position-arl/position-arl.model';
import { IPeriod } from 'app/entities/period/period.model';
import { IOperatorType } from 'app/entities/operator-type/operator-type.model';
import { IOperatorMatriz } from 'app/entities/operator-matriz/operator-matriz.model';
import { ISocialSecurity } from 'app/entities/social-security/social-security.model';
import { IProjectMaster } from 'app/entities/project-master/project-master.model';
import { IIncome } from 'app/entities/income/income.model';
import { IDeduction } from 'app/entities/deduction/deduction.model';
import { IDocumentType } from 'app/entities/document-type/document-type.model';
import { StateEmployee } from 'app/entities/enumerations/state-employee.model';

export interface IEmployee {
  id?: number;
  completeName?: string;
  address?: string;
  dateStart?: dayjs.Dayjs;
  city?: string;
  mobile?: number;
  stateEmployee?: StateEmployee;
  user?: IUser;
  contract?: IContract;
  allergy?: IAllergy;
  socialPayments?: ISocialPayments;
  positionArl?: IPositionArl;
  period?: IPeriod;
  operatorType?: IOperatorType;
  operatorMatriz?: IOperatorMatriz;
  socialSecurity?: ISocialSecurity;
  employees?: IProjectMaster[];
  income?: IIncome | null;
  deduction?: IDeduction | null;
  documentType?: IDocumentType | null;
}

export class Employee implements IEmployee {
  constructor(
    public id?: number,
    public completeName?: string,
    public address?: string,
    public dateStart?: dayjs.Dayjs,
    public city?: string,
    public mobile?: number,
    public stateEmployee?: StateEmployee,
    public user?: IUser,
    public contract?: IContract,
    public allergy?: IAllergy,
    public socialPayments?: ISocialPayments,
    public positionArl?: IPositionArl,
    public period?: IPeriod,
    public operatorType?: IOperatorType,
    public operatorMatriz?: IOperatorMatriz,
    public socialSecurity?: ISocialSecurity,
    public employees?: IProjectMaster[],
    public income?: IIncome | null,
    public deduction?: IDeduction | null,
    public documentType?: IDocumentType | null
  ) {}
}

export function getEmployeeIdentifier(employee: IEmployee): number | undefined {
  return employee.id;
}
