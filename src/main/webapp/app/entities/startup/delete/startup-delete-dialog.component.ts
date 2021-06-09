import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IStartup } from '../startup.model';
import { StartupService } from '../service/startup.service';

@Component({
  templateUrl: './startup-delete-dialog.component.html',
})
export class StartupDeleteDialogComponent {
  startup?: IStartup;

  constructor(protected startupService: StartupService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.startupService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
