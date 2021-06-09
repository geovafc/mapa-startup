import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStartup, Startup } from '../startup.model';
import { StartupService } from '../service/startup.service';

@Injectable({ providedIn: 'root' })
export class StartupRoutingResolveService implements Resolve<IStartup> {
  constructor(protected service: StartupService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStartup> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((startup: HttpResponse<Startup>) => {
          if (startup.body) {
            return of(startup.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Startup());
  }
}
