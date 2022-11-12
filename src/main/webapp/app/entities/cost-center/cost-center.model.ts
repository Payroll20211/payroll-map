export interface ICostCenter {
  id?: number;
  costCenterCode?: string;
  costCenterName?: string;
  costCenterType?: string;
}

export class CostCenter implements ICostCenter {
  constructor(public id?: number, public costCenterCode?: string, public costCenterName?: string, public costCenterType?: string) {}
}

export function getCostCenterIdentifier(costCenter: ICostCenter): number | undefined {
  return costCenter.id;
}
