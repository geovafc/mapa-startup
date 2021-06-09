import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMembro, Membro } from '../membro.model';

import { MembroService } from './membro.service';

describe('Service Tests', () => {
  describe('Membro Service', () => {
    let service: MembroService;
    let httpMock: HttpTestingController;
    let elemDefault: IMembro;
    let expectedResult: IMembro | IMembro[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MembroService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nome: 'AAAAAAA',
        funcao: 'AAAAAAA',
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

      it('should create a Membro', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Membro()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Membro', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            funcao: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Membro', () => {
        const patchObject = Object.assign(
          {
            funcao: 'BBBBBB',
          },
          new Membro()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Membro', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            funcao: 'BBBBBB',
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

      it('should delete a Membro', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMembroToCollectionIfMissing', () => {
        it('should add a Membro to an empty array', () => {
          const membro: IMembro = { id: 123 };
          expectedResult = service.addMembroToCollectionIfMissing([], membro);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(membro);
        });

        it('should not add a Membro to an array that contains it', () => {
          const membro: IMembro = { id: 123 };
          const membroCollection: IMembro[] = [
            {
              ...membro,
            },
            { id: 456 },
          ];
          expectedResult = service.addMembroToCollectionIfMissing(membroCollection, membro);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Membro to an array that doesn't contain it", () => {
          const membro: IMembro = { id: 123 };
          const membroCollection: IMembro[] = [{ id: 456 }];
          expectedResult = service.addMembroToCollectionIfMissing(membroCollection, membro);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(membro);
        });

        it('should add only unique Membro to an array', () => {
          const membroArray: IMembro[] = [{ id: 123 }, { id: 456 }, { id: 67618 }];
          const membroCollection: IMembro[] = [{ id: 123 }];
          expectedResult = service.addMembroToCollectionIfMissing(membroCollection, ...membroArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const membro: IMembro = { id: 123 };
          const membro2: IMembro = { id: 456 };
          expectedResult = service.addMembroToCollectionIfMissing([], membro, membro2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(membro);
          expect(expectedResult).toContain(membro2);
        });

        it('should accept null and undefined values', () => {
          const membro: IMembro = { id: 123 };
          expectedResult = service.addMembroToCollectionIfMissing([], null, membro, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(membro);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
