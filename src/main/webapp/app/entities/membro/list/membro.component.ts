import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMembro } from '../membro.model';
import { MembroService } from '../service/membro.service';
import { MembroDeleteDialogComponent } from '../delete/membro-delete-dialog.component';

@Component({
  selector: 'jhi-membro',
  templateUrl: './membro.component.html',
})
export class MembroComponent implements OnInit {
  membros?: IMembro[];
  isLoading = false;

  constructor(protected membroService: MembroService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.membroService.query().subscribe(
      (res: HttpResponse<IMembro[]>) => {
        this.isLoading = false;
        this.membros = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IMembro): number {
    return item.id!;
  }

  delete(membro: IMembro): void {
    const modalRef = this.modalService.open(MembroDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.membro = membro;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
