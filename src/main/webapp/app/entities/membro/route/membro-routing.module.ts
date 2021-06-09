import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MembroComponent } from '../list/membro.component';
import { MembroDetailComponent } from '../detail/membro-detail.component';
import { MembroUpdateComponent } from '../update/membro-update.component';
import { MembroRoutingResolveService } from './membro-routing-resolve.service';

const membroRoute: Routes = [
  {
    path: '',
    component: MembroComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MembroDetailComponent,
    resolve: {
      membro: MembroRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MembroUpdateComponent,
    resolve: {
      membro: MembroRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MembroUpdateComponent,
    resolve: {
      membro: MembroRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(membroRoute)],
  exports: [RouterModule],
})
export class MembroRoutingModule {}
