import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MembroService } from '../service/membro.service';

import { MembroComponent } from './membro.component';

describe('Component Tests', () => {
  describe('Membro Management Component', () => {
    let comp: MembroComponent;
    let fixture: ComponentFixture<MembroComponent>;
    let service: MembroService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MembroComponent],
      })
        .overrideTemplate(MembroComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MembroComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(MembroService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.membros?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
