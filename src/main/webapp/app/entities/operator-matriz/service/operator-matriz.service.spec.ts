import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOperatorMatriz, OperatorMatriz } from '../operator-matriz.model';

import { OperatorMatrizService } from './operator-matriz.service';

describe('OperatorMatriz Service', () => {
  let service: OperatorMatrizService;
  let httpMock: HttpTestingController;
  let elemDefault: IOperatorMatriz;
  let expectedResult: IOperatorMatriz | IOperatorMatriz[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OperatorMatrizService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      numberid: 0,
      digitverification: 0,
      name: 'AAAAAAA',
      address: 'AAAAAAA',
      city: 'AAAAAAA',
      email: 'AAAAAAA',
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

    it('should create a OperatorMatriz', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new OperatorMatriz()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OperatorMatriz', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numberid: 1,
          digitverification: 1,
          name: 'BBBBBB',
          address: 'BBBBBB',
          city: 'BBBBBB',
          email: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OperatorMatriz', () => {
      const patchObject = Object.assign(
        {
          digitverification: 1,
          email: 'BBBBBB',
        },
        new OperatorMatriz()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OperatorMatriz', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numberid: 1,
          digitverification: 1,
          name: 'BBBBBB',
          address: 'BBBBBB',
          city: 'BBBBBB',
          email: 'BBBBBB',
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

    it('should delete a OperatorMatriz', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addOperatorMatrizToCollectionIfMissing', () => {
      it('should add a OperatorMatriz to an empty array', () => {
        const operatorMatriz: IOperatorMatriz = { id: 123 };
        expectedResult = service.addOperatorMatrizToCollectionIfMissing([], operatorMatriz);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(operatorMatriz);
      });

      it('should not add a OperatorMatriz to an array that contains it', () => {
        const operatorMatriz: IOperatorMatriz = { id: 123 };
        const operatorMatrizCollection: IOperatorMatriz[] = [
          {
            ...operatorMatriz,
          },
          { id: 456 },
        ];
        expectedResult = service.addOperatorMatrizToCollectionIfMissing(operatorMatrizCollection, operatorMatriz);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OperatorMatriz to an array that doesn't contain it", () => {
        const operatorMatriz: IOperatorMatriz = { id: 123 };
        const operatorMatrizCollection: IOperatorMatriz[] = [{ id: 456 }];
        expectedResult = service.addOperatorMatrizToCollectionIfMissing(operatorMatrizCollection, operatorMatriz);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(operatorMatriz);
      });

      it('should add only unique OperatorMatriz to an array', () => {
        const operatorMatrizArray: IOperatorMatriz[] = [{ id: 123 }, { id: 456 }, { id: 72903 }];
        const operatorMatrizCollection: IOperatorMatriz[] = [{ id: 123 }];
        expectedResult = service.addOperatorMatrizToCollectionIfMissing(operatorMatrizCollection, ...operatorMatrizArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const operatorMatriz: IOperatorMatriz = { id: 123 };
        const operatorMatriz2: IOperatorMatriz = { id: 456 };
        expectedResult = service.addOperatorMatrizToCollectionIfMissing([], operatorMatriz, operatorMatriz2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(operatorMatriz);
        expect(expectedResult).toContain(operatorMatriz2);
      });

      it('should accept null and undefined values', () => {
        const operatorMatriz: IOperatorMatriz = { id: 123 };
        expectedResult = service.addOperatorMatrizToCollectionIfMissing([], null, operatorMatriz, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(operatorMatriz);
      });

      it('should return initial array if no OperatorMatriz is added', () => {
        const operatorMatrizCollection: IOperatorMatriz[] = [{ id: 123 }];
        expectedResult = service.addOperatorMatrizToCollectionIfMissing(operatorMatrizCollection, undefined, null);
        expect(expectedResult).toEqual(operatorMatrizCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
