import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMembro, Membro } from '../membro.model';
import { MembroService } from '../service/membro.service';

@Injectable({ providedIn: 'root' })
export class MembroRoutingResolveService implements Resolve<IMembro> {
  constructor(protected service: MembroService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMembro> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((membro: HttpResponse<Membro>) => {
          if (membro.body) {
            return of(membro.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Membro());
  }
}
