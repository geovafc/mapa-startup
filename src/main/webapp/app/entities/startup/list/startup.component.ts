import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IStartup } from '../startup.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { StartupService } from '../service/startup.service';
import { StartupDeleteDialogComponent } from '../delete/startup-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-startup',
  templateUrl: './startup.component.html',
})
export class StartupComponent implements OnInit {
  startups: IStartup[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected startupService: StartupService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.startups = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.startupService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IStartup[]>) => {
          this.isLoading = false;
          this.paginateStartups(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.startups = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IStartup): number {
    return item.id!;
  }

  delete(startup: IStartup): void {
    const modalRef = this.modalService.open(StartupDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.startup = startup;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateStartups(data: IStartup[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.startups.push(d);
      }
    }
  }
}
