export interface ISocialSecurity {
  id?: number;
  eps?: string;
  afp?: string;
}

export class SocialSecurity implements ISocialSecurity {
  constructor(public id?: number, public eps?: string, public afp?: string) {}
}

export function getSocialSecurityIdentifier(socialSecurity: ISocialSecurity): number | undefined {
  return socialSecurity.id;
}
