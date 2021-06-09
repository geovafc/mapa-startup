import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMembro } from '../membro.model';
import { MembroService } from '../service/membro.service';

@Component({
  templateUrl: './membro-delete-dialog.component.html',
})
export class MembroDeleteDialogComponent {
  membro?: IMembro;

  constructor(protected membroService: MembroService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.membroService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
