export interface IContract {
  id?: number;
  salary?: number;
}

export class Contract implements IContract {
  constructor(public id?: number, public salary?: number) {}
}

export function getContractIdentifier(contract: IContract): number | undefined {
  return contract.id;
}
