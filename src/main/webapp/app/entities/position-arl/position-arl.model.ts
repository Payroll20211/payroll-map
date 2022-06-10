export interface IPositionArl {
  id?: number;
  riskClass?: number;
  positionCode?: string;
  position?: string;
}

export class PositionArl implements IPositionArl {
  constructor(public id?: number, public riskClass?: number, public positionCode?: string, public position?: string) {}
}

export function getPositionArlIdentifier(positionArl: IPositionArl): number | undefined {
  return positionArl.id;
}
