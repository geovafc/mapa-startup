import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StartupComponent } from '../list/startup.component';
import { StartupDetailComponent } from '../detail/startup-detail.component';
import { StartupUpdateComponent } from '../update/startup-update.component';
import { StartupRoutingResolveService } from './startup-routing-resolve.service';

const startupRoute: Routes = [
  {
    path: '',
    component: StartupComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StartupDetailComponent,
    resolve: {
      startup: StartupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StartupUpdateComponent,
    resolve: {
      startup: StartupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StartupUpdateComponent,
    resolve: {
      startup: StartupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(startupRoute)],
  exports: [RouterModule],
})
export class StartupRoutingModule {}
