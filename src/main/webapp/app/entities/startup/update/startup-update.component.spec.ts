jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { StartupService } from '../service/startup.service';
import { IStartup, Startup } from '../startup.model';

import { StartupUpdateComponent } from './startup-update.component';

describe('Component Tests', () => {
  describe('Startup Management Update Component', () => {
    let comp: StartupUpdateComponent;
    let fixture: ComponentFixture<StartupUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let startupService: StartupService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [StartupUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(StartupUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StartupUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      startupService = TestBed.inject(StartupService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const startup: IStartup = { id: 456 };

        activatedRoute.data = of({ startup });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(startup));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const startup = { id: 123 };
        spyOn(startupService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ startup });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: startup }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(startupService.update).toHaveBeenCalledWith(startup);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const startup = new Startup();
        spyOn(startupService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ startup });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: startup }));
        saveSubject.complete();

        // THEN
        expect(startupService.create).toHaveBeenCalledWith(startup);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const startup = { id: 123 };
        spyOn(startupService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ startup });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(startupService.update).toHaveBeenCalledWith(startup);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
