import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IStartup, Startup } from '../startup.model';

import { StartupService } from './startup.service';

describe('Service Tests', () => {
  describe('Startup Service', () => {
    let service: StartupService;
    let httpMock: HttpTestingController;
    let elemDefault: IStartup;
    let expectedResult: IStartup | IStartup[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(StartupService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nome: 'AAAAAAA',
        segmento: 'AAAAAAA',
        descricao: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Startup', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Startup()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Startup', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            segmento: 'BBBBBB',
            descricao: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Startup', () => {
        const patchObject = Object.assign(
          {
            nome: 'BBBBBB',
            segmento: 'BBBBBB',
            descricao: 'BBBBBB',
          },
          new Startup()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Startup', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            segmento: 'BBBBBB',
            descricao: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Startup', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addStartupToCollectionIfMissing', () => {
        it('should add a Startup to an empty array', () => {
          const startup: IStartup = { id: 123 };
          expectedResult = service.addStartupToCollectionIfMissing([], startup);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(startup);
        });

        it('should not add a Startup to an array that contains it', () => {
          const startup: IStartup = { id: 123 };
          const startupCollection: IStartup[] = [
            {
              ...startup,
            },
            { id: 456 },
          ];
          expectedResult = service.addStartupToCollectionIfMissing(startupCollection, startup);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Startup to an array that doesn't contain it", () => {
          const startup: IStartup = { id: 123 };
          const startupCollection: IStartup[] = [{ id: 456 }];
          expectedResult = service.addStartupToCollectionIfMissing(startupCollection, startup);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(startup);
        });

        it('should add only unique Startup to an array', () => {
          const startupArray: IStartup[] = [{ id: 123 }, { id: 456 }, { id: 16238 }];
          const startupCollection: IStartup[] = [{ id: 123 }];
          expectedResult = service.addStartupToCollectionIfMissing(startupCollection, ...startupArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const startup: IStartup = { id: 123 };
          const startup2: IStartup = { id: 456 };
          expectedResult = service.addStartupToCollectionIfMissing([], startup, startup2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(startup);
          expect(expectedResult).toContain(startup2);
        });

        it('should accept null and undefined values', () => {
          const startup: IStartup = { id: 123 };
          expectedResult = service.addStartupToCollectionIfMissing([], null, startup, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(startup);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
