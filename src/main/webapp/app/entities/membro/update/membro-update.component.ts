import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMembro, Membro } from '../membro.model';
import { MembroService } from '../service/membro.service';
import { IStartup } from 'app/entities/startup/startup.model';
import { StartupService } from 'app/entities/startup/service/startup.service';

@Component({
  selector: 'jhi-membro-update',
  templateUrl: './membro-update.component.html',
})
export class MembroUpdateComponent implements OnInit {
  isSaving = false;

  startupsSharedCollection: IStartup[] = [];

  editForm = this.fb.group({
    id: [],
    nome: [],
    funcao: [],
    startup: [],
  });

  constructor(
    protected membroService: MembroService,
    protected startupService: StartupService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ membro }) => {
      this.updateForm(membro);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const membro = this.createFromForm();
    if (membro.id !== undefined) {
      this.subscribeToSaveResponse(this.membroService.update(membro));
    } else {
      this.subscribeToSaveResponse(this.membroService.create(membro));
    }
  }

  trackStartupById(index: number, item: IStartup): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMembro>>): void {
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

  protected updateForm(membro: IMembro): void {
    this.editForm.patchValue({
      id: membro.id,
      nome: membro.nome,
      funcao: membro.funcao,
      startup: membro.startup,
    });

    this.startupsSharedCollection = this.startupService.addStartupToCollectionIfMissing(this.startupsSharedCollection, membro.startup);
  }

  protected loadRelationshipsOptions(): void {
    this.startupService
      .query()
      .pipe(map((res: HttpResponse<IStartup[]>) => res.body ?? []))
      .pipe(
        map((startups: IStartup[]) => this.startupService.addStartupToCollectionIfMissing(startups, this.editForm.get('startup')!.value))
      )
      .subscribe((startups: IStartup[]) => (this.startupsSharedCollection = startups));
  }

  protected createFromForm(): IMembro {
    return {
      ...new Membro(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      funcao: this.editForm.get(['funcao'])!.value,
      startup: this.editForm.get(['startup'])!.value,
    };
  }
}
