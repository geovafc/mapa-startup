import { IStartup } from 'app/entities/startup/startup.model';

export interface IMembro {
  id?: number;
  nome?: string | null;
  funcao?: string | null;
  startup?: IStartup | null;
}

export class Membro implements IMembro {
  constructor(public id?: number, public nome?: string | null, public funcao?: string | null, public startup?: IStartup | null) {}
}

export function getMembroIdentifier(membro: IMembro): number | undefined {
  return membro.id;
}
