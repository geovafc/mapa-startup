import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMembro } from '../membro.model';

@Component({
  selector: 'jhi-membro-detail',
  templateUrl: './membro-detail.component.html',
})
export class MembroDetailComponent implements OnInit {
  membro: IMembro | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ membro }) => {
      this.membro = membro;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
