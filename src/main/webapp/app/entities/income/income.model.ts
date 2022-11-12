import { IEmployee } from 'app/entities/employee/employee.model';
import { IAccountPlan } from 'app/entities/account-plan/account-plan.model';

export interface IIncome {
  id?: number;
  incomeCode?: string;
  description?: string;
  incomes?: IEmployee[];
  accountPlans?: IAccountPlan[] | null;
}

export class Income implements IIncome {
  constructor(
    public id?: number,
    public incomeCode?: string,
    public description?: string,
    public incomes?: IEmployee[],
    public accountPlans?: IAccountPlan[] | null
  ) {}
}

export function getIncomeIdentifier(income: IIncome): number | undefined {
  return income.id;
}
