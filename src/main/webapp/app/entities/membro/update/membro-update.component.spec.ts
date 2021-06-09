jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MembroService } from '../service/membro.service';
import { IMembro, Membro } from '../membro.model';
import { IStartup } from 'app/entities/startup/startup.model';
import { StartupService } from 'app/entities/startup/service/startup.service';

import { MembroUpdateComponent } from './membro-update.component';

describe('Component Tests', () => {
  describe('Membro Management Update Component', () => {
    let comp: MembroUpdateComponent;
    let fixture: ComponentFixture<MembroUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let membroService: MembroService;
    let startupService: StartupService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MembroUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MembroUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MembroUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      membroService = TestBed.inject(MembroService);
      startupService = TestBed.inject(StartupService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Startup query and add missing value', () => {
        const membro: IMembro = { id: 456 };
        const startup: IStartup = { id: 88308 };
        membro.startup = startup;

        const startupCollection: IStartup[] = [{ id: 21435 }];
        spyOn(startupService, 'query').and.returnValue(of(new HttpResponse({ body: startupCollection })));
        const additionalStartups = [startup];
        const expectedCollection: IStartup[] = [...additionalStartups, ...startupCollection];
        spyOn(startupService, 'addStartupToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ membro });
        comp.ngOnInit();

        expect(startupService.query).toHaveBeenCalled();
        expect(startupService.addStartupToCollectionIfMissing).toHaveBeenCalledWith(startupCollection, ...additionalStartups);
        expect(comp.startupsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const membro: IMembro = { id: 456 };
        const startup: IStartup = { id: 65307 };
        membro.startup = startup;

        activatedRoute.data = of({ membro });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(membro));
        expect(comp.startupsSharedCollection).toContain(startup);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const membro = { id: 123 };
        spyOn(membroService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ membro });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: membro }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(membroService.update).toHaveBeenCalledWith(membro);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const membro = new Membro();
        spyOn(membroService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ membro });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: membro }));
        saveSubject.complete();

        // THEN
        expect(membroService.create).toHaveBeenCalledWith(membro);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const membro = { id: 123 };
        spyOn(membroService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ membro });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(membroService.update).toHaveBeenCalledWith(membro);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackStartupById', () => {
        it('Should return tracked Startup primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackStartupById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
