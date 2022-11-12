import { IEmployee } from 'app/entities/employee/employee.model';
import { IAccountPlan } from 'app/entities/account-plan/account-plan.model';

export interface IDeduction {
  id?: number;
  deductionCode?: string;
  description?: string;
  deductions?: IEmployee[];
  accountPlans?: IAccountPlan[] | null;
}

export class Deduction implements IDeduction {
  constructor(
    public id?: number,
    public deductionCode?: string,
    public description?: string,
    public deductions?: IEmployee[],
    public accountPlans?: IAccountPlan[] | null
  ) {}
}

export function getDeductionIdentifier(deduction: IDeduction): number | undefined {
  return deduction.id;
}
