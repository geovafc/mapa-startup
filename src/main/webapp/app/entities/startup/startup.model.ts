import { IMembro } from 'app/entities/membro/membro.model';

export interface IStartup {
  id?: number;
  nome?: string | null;
  segmento?: string | null;
  descricao?: string | null;
  membros?: IMembro[] | null;
}

export class Startup implements IStartup {
  constructor(
    public id?: number,
    public nome?: string | null,
    public segmento?: string | null,
    public descricao?: string | null,
    public membros?: IMembro[] | null
  ) {}
}

export function getStartupIdentifier(startup: IStartup): number | undefined {
  return startup.id;
}
