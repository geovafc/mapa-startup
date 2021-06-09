import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'startup',
        data: { pageTitle: 'mapaStartupApp.startup.home.title' },
        loadChildren: () => import('./startup/startup.module').then(m => m.StartupModule),
      },
      {
        path: 'membro',
        data: { pageTitle: 'mapaStartupApp.membro.home.title' },
        loadChildren: () => import('./membro/membro.module').then(m => m.MembroModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
