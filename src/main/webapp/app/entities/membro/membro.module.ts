import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MembroComponent } from './list/membro.component';
import { MembroDetailComponent } from './detail/membro-detail.component';
import { MembroUpdateComponent } from './update/membro-update.component';
import { MembroDeleteDialogComponent } from './delete/membro-delete-dialog.component';
import { MembroRoutingModule } from './route/membro-routing.module';

@NgModule({
  imports: [SharedModule, MembroRoutingModule],
  declarations: [MembroComponent, MembroDetailComponent, MembroUpdateComponent, MembroDeleteDialogComponent],
  entryComponents: [MembroDeleteDialogComponent],
})
export class MembroModule {}
