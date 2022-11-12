export interface IPeriod {
  id?: number;
  periodCode?: string;
  description?: string;
}

export class Period implements IPeriod {
  constructor(public id?: number, public periodCode?: string, public description?: string) {}
}

export function getPeriodIdentifier(period: IPeriod): number | undefined {
  return period.id;
}
