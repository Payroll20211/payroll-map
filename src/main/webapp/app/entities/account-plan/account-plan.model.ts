import { IIncome } from 'app/entities/income/income.model';
import { IDeduction } from 'app/entities/deduction/deduction.model';

export interface IAccountPlan {
  id?: number;
  code?: string;
  description?: string;
  incomes?: IIncome[] | null;
  deductions?: IDeduction[] | null;
}

export class AccountPlan implements IAccountPlan {
  constructor(
    public id?: number,
    public code?: string,
    public description?: string,
    public incomes?: IIncome[] | null,
    public deductions?: IDeduction[] | null
  ) {}
}

export function getAccountPlanIdentifier(accountPlan: IAccountPlan): number | undefined {
  return accountPlan.id;
}
