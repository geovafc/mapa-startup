import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStartup, getStartupIdentifier } from '../startup.model';

export type EntityResponseType = HttpResponse<IStartup>;
export type EntityArrayResponseType = HttpResponse<IStartup[]>;

@Injectable({ providedIn: 'root' })
export class StartupService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/startups');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(startup: IStartup): Observable<EntityResponseType> {
    return this.http.post<IStartup>(this.resourceUrl, startup, { observe: 'response' });
  }

  update(startup: IStartup): Observable<EntityResponseType> {
    return this.http.put<IStartup>(`${this.resourceUrl}/${getStartupIdentifier(startup) as number}`, startup, { observe: 'response' });
  }

  partialUpdate(startup: IStartup): Observable<EntityResponseType> {
    return this.http.patch<IStartup>(`${this.resourceUrl}/${getStartupIdentifier(startup) as number}`, startup, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStartup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStartup[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addStartupToCollectionIfMissing(startupCollection: IStartup[], ...startupsToCheck: (IStartup | null | undefined)[]): IStartup[] {
    const startups: IStartup[] = startupsToCheck.filter(isPresent);
    if (startups.length > 0) {
      const startupCollectionIdentifiers = startupCollection.map(startupItem => getStartupIdentifier(startupItem)!);
      const startupsToAdd = startups.filter(startupItem => {
        const startupIdentifier = getStartupIdentifier(startupItem);
        if (startupIdentifier == null || startupCollectionIdentifiers.includes(startupIdentifier)) {
          return false;
        }
        startupCollectionIdentifiers.push(startupIdentifier);
        return true;
      });
      return [...startupsToAdd, ...startupCollection];
    }
    return startupCollection;
  }
}
