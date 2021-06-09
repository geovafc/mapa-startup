import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMembro, getMembroIdentifier } from '../membro.model';

export type EntityResponseType = HttpResponse<IMembro>;
export type EntityArrayResponseType = HttpResponse<IMembro[]>;

@Injectable({ providedIn: 'root' })
export class MembroService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/membros');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(membro: IMembro): Observable<EntityResponseType> {
    return this.http.post<IMembro>(this.resourceUrl, membro, { observe: 'response' });
  }

  update(membro: IMembro): Observable<EntityResponseType> {
    return this.http.put<IMembro>(`${this.resourceUrl}/${getMembroIdentifier(membro) as number}`, membro, { observe: 'response' });
  }

  partialUpdate(membro: IMembro): Observable<EntityResponseType> {
    return this.http.patch<IMembro>(`${this.resourceUrl}/${getMembroIdentifier(membro) as number}`, membro, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMembro>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMembro[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMembroToCollectionIfMissing(membroCollection: IMembro[], ...membrosToCheck: (IMembro | null | undefined)[]): IMembro[] {
    const membros: IMembro[] = membrosToCheck.filter(isPresent);
    if (membros.length > 0) {
      const membroCollectionIdentifiers = membroCollection.map(membroItem => getMembroIdentifier(membroItem)!);
      const membrosToAdd = membros.filter(membroItem => {
        const membroIdentifier = getMembroIdentifier(membroItem);
        if (membroIdentifier == null || membroCollectionIdentifiers.includes(membroIdentifier)) {
          return false;
        }
        membroCollectionIdentifiers.push(membroIdentifier);
        return true;
      });
      return [...membrosToAdd, ...membroCollection];
    }
    return membroCollection;
  }
}
