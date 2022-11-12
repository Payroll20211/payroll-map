import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOperatorType, OperatorType } from '../operator-type.model';

import { OperatorTypeService } from './operator-type.service';

describe('OperatorType Service', () => {
  let service: OperatorTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IOperatorType;
  let expectedResult: IOperatorType | IOperatorType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OperatorTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      code: 'AAAAAAA',
      description: 'AAAAAAA',
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

    it('should create a OperatorType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new OperatorType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OperatorType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          code: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OperatorType', () => {
      const patchObject = Object.assign(
        {
          code: 'BBBBBB',
          description: 'BBBBBB',
        },
        new OperatorType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OperatorType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          code: 'BBBBBB',
          description: 'BBBBBB',
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

    it('should delete a OperatorType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addOperatorTypeToCollectionIfMissing', () => {
      it('should add a OperatorType to an empty array', () => {
        const operatorType: IOperatorType = { id: 123 };
        expectedResult = service.addOperatorTypeToCollectionIfMissing([], operatorType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(operatorType);
      });

      it('should not add a OperatorType to an array that contains it', () => {
        const operatorType: IOperatorType = { id: 123 };
        const operatorTypeCollection: IOperatorType[] = [
          {
            ...operatorType,
          },
          { id: 456 },
        ];
        expectedResult = service.addOperatorTypeToCollectionIfMissing(operatorTypeCollection, operatorType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OperatorType to an array that doesn't contain it", () => {
        const operatorType: IOperatorType = { id: 123 };
        const operatorTypeCollection: IOperatorType[] = [{ id: 456 }];
        expectedResult = service.addOperatorTypeToCollectionIfMissing(operatorTypeCollection, operatorType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(operatorType);
      });

      it('should add only unique OperatorType to an array', () => {
        const operatorTypeArray: IOperatorType[] = [{ id: 123 }, { id: 456 }, { id: 35012 }];
        const operatorTypeCollection: IOperatorType[] = [{ id: 123 }];
        expectedResult = service.addOperatorTypeToCollectionIfMissing(operatorTypeCollection, ...operatorTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const operatorType: IOperatorType = { id: 123 };
        const operatorType2: IOperatorType = { id: 456 };
        expectedResult = service.addOperatorTypeToCollectionIfMissing([], operatorType, operatorType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(operatorType);
        expect(expectedResult).toContain(operatorType2);
      });

      it('should accept null and undefined values', () => {
        const operatorType: IOperatorType = { id: 123 };
        expectedResult = service.addOperatorTypeToCollectionIfMissing([], null, operatorType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(operatorType);
      });

      it('should return initial array if no OperatorType is added', () => {
        const operatorTypeCollection: IOperatorType[] = [{ id: 123 }];
        expectedResult = service.addOperatorTypeToCollectionIfMissing(operatorTypeCollection, undefined, null);
        expect(expectedResult).toEqual(operatorTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
