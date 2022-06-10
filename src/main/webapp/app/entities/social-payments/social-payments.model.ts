export interface ISocialPayments {
  id?: number;
  code?: string;
  description?: string;
}

export class SocialPayments implements ISocialPayments {
  constructor(public id?: number, public code?: string, public description?: string) {}
}

export function getSocialPaymentsIdentifier(socialPayments: ISocialPayments): number | undefined {
  return socialPayments.id;
}
