import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStartup } from '../startup.model';

@Component({
  selector: 'jhi-startup-detail',
  templateUrl: './startup-detail.component.html',
})
export class StartupDetailComponent implements OnInit {
  startup: IStartup | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ startup }) => {
      this.startup = startup;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
