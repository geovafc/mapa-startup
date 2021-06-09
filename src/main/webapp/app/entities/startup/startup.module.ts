import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { StartupComponent } from './list/startup.component';
import { StartupDetailComponent } from './detail/startup-detail.component';
import { StartupUpdateComponent } from './update/startup-update.component';
import { StartupDeleteDialogComponent } from './delete/startup-delete-dialog.component';
import { StartupRoutingModule } from './route/startup-routing.module';

@NgModule({
  imports: [SharedModule, StartupRoutingModule],
  declarations: [StartupComponent, StartupDetailComponent, StartupUpdateComponent, StartupDeleteDialogComponent],
  entryComponents: [StartupDeleteDialogComponent],
})
export class StartupModule {}
