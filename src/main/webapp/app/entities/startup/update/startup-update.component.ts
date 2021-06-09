import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IStartup, Startup } from '../startup.model';
import { StartupService } from '../service/startup.service';

@Component({
  selector: 'jhi-startup-update',
  templateUrl: './startup-update.component.html',
})
export class StartupUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nome: [],
    segmento: [],
    descricao: [],
  });

  constructor(protected startupService: StartupService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ startup }) => {
      this.updateForm(startup);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const startup = this.createFromForm();
    if (startup.id !== undefined) {
      this.subscribeToSaveResponse(this.startupService.update(startup));
    } else {
      this.subscribeToSaveResponse(this.startupService.create(startup));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStartup>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(startup: IStartup): void {
    this.editForm.patchValue({
      id: startup.id,
      nome: startup.nome,
      segmento: startup.segmento,
      descricao: startup.descricao,
    });
  }

  protected createFromForm(): IStartup {
    return {
      ...new Startup(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      segmento: this.editForm.get(['segmento'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
    };
  }
}
