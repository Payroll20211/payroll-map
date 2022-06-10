export interface IAllergy {
  id?: number;
  description?: string;
  treatment?: string;
}

export class Allergy implements IAllergy {
  constructor(public id?: number, public description?: string, public treatment?: string) {}
}

export function getAllergyIdentifier(allergy: IAllergy): number | undefined {
  return allergy.id;
}
