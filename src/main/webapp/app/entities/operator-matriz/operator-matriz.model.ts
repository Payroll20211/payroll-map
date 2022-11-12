export interface IOperatorMatriz {
  id?: number;
  numberid?: number | null;
  digitverification?: number | null;
  name?: string;
  address?: string;
  city?: string;
  email?: string;
}

export class OperatorMatriz implements IOperatorMatriz {
  constructor(
    public id?: number,
    public numberid?: number | null,
    public digitverification?: number | null,
    public name?: string,
    public address?: string,
    public city?: string,
    public email?: string
  ) {}
}

export function getOperatorMatrizIdentifier(operatorMatriz: IOperatorMatriz): number | undefined {
  return operatorMatriz.id;
}
