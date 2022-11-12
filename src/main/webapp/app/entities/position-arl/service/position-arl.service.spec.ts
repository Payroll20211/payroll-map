import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPositionArl, PositionArl } from '../position-arl.model';

import { PositionArlService } from './position-arl.service';

describe('PositionArl Service', () => {
  let service: PositionArlService;
  let httpMock: HttpTestingController;
  let elemDefault: IPositionArl;
  let expectedResult: IPositionArl | IPositionArl[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PositionArlService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      riskClass: 0,
      positionCode: 'AAAAAAA',
      position: 'AAAAAAA',
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

    it('should create a PositionArl', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PositionArl()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PositionArl', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          riskClass: 1,
          positionCode: 'BBBBBB',
          position: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PositionArl', () => {
      const patchObject = Object.assign(
        {
          riskClass: 1,
        },
        new PositionArl()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PositionArl', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          riskClass: 1,
          positionCode: 'BBBBBB',
          position: 'BBBBBB',
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

    it('should delete a PositionArl', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPositionArlToCollectionIfMissing', () => {
      it('should add a PositionArl to an empty array', () => {
        const positionArl: IPositionArl = { id: 123 };
        expectedResult = service.addPositionArlToCollectionIfMissing([], positionArl);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(positionArl);
      });

      it('should not add a PositionArl to an array that contains it', () => {
        const positionArl: IPositionArl = { id: 123 };
        const positionArlCollection: IPositionArl[] = [
          {
            ...positionArl,
          },
          { id: 456 },
        ];
        expectedResult = service.addPositionArlToCollectionIfMissing(positionArlCollection, positionArl);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PositionArl to an array that doesn't contain it", () => {
        const positionArl: IPositionArl = { id: 123 };
        const positionArlCollection: IPositionArl[] = [{ id: 456 }];
        expectedResult = service.addPositionArlToCollectionIfMissing(positionArlCollection, positionArl);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(positionArl);
      });

      it('should add only unique PositionArl to an array', () => {
        const positionArlArray: IPositionArl[] = [{ id: 123 }, { id: 456 }, { id: 81074 }];
        const positionArlCollection: IPositionArl[] = [{ id: 123 }];
        expectedResult = service.addPositionArlToCollectionIfMissing(positionArlCollection, ...positionArlArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const positionArl: IPositionArl = { id: 123 };
        const positionArl2: IPositionArl = { id: 456 };
        expectedResult = service.addPositionArlToCollectionIfMissing([], positionArl, positionArl2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(positionArl);
        expect(expectedResult).toContain(positionArl2);
      });

      it('should accept null and undefined values', () => {
        const positionArl: IPositionArl = { id: 123 };
        expectedResult = service.addPositionArlToCollectionIfMissing([], null, positionArl, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(positionArl);
      });

      it('should return initial array if no PositionArl is added', () => {
        const positionArlCollection: IPositionArl[] = [{ id: 123 }];
        expectedResult = service.addPositionArlToCollectionIfMissing(positionArlCollection, undefined, null);
        expect(expectedResult).toEqual(positionArlCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
