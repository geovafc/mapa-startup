import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StartupDetailComponent } from './startup-detail.component';

describe('Component Tests', () => {
  describe('Startup Management Detail Component', () => {
    let comp: StartupDetailComponent;
    let fixture: ComponentFixture<StartupDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [StartupDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ startup: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(StartupDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StartupDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load startup on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.startup).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
