import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MembroDetailComponent } from './membro-detail.component';

describe('Component Tests', () => {
  describe('Membro Management Detail Component', () => {
    let comp: MembroDetailComponent;
    let fixture: ComponentFixture<MembroDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MembroDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ membro: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MembroDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MembroDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load membro on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.membro).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
