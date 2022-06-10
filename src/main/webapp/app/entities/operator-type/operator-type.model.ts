export interface IOperatorType {
  id?: number;
  code?: string;
  description?: string;
}

export class OperatorType implements IOperatorType {
  constructor(public id?: number, public code?: string, public description?: string) {}
}

export function getOperatorTypeIdentifier(operatorType: IOperatorType): number | undefined {
  return operatorType.id;
}
