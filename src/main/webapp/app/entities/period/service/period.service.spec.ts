import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPeriod, Period } from '../period.model';

import { PeriodService } from './period.service';

describe('Period Service', () => {
  let service: PeriodService;
  let httpMock: HttpTestingController;
  let elemDefault: IPeriod;
  let expectedResult: IPeriod | IPeriod[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PeriodService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      periodCode: 'AAAAAAA',
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

    it('should create a Period', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Period()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Period', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          periodCode: 'BBBBBB',
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

    it('should partial update a Period', () => {
      const patchObject = Object.assign(
        {
          periodCode: 'BBBBBB',
          description: 'BBBBBB',
        },
        new Period()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Period', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          periodCode: 'BBBBBB',
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

    it('should delete a Period', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPeriodToCollectionIfMissing', () => {
      it('should add a Period to an empty array', () => {
        const period: IPeriod = { id: 123 };
        expectedResult = service.addPeriodToCollectionIfMissing([], period);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(period);
      });

      it('should not add a Period to an array that contains it', () => {
        const period: IPeriod = { id: 123 };
        const periodCollection: IPeriod[] = [
          {
            ...period,
          },
          { id: 456 },
        ];
        expectedResult = service.addPeriodToCollectionIfMissing(periodCollection, period);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Period to an array that doesn't contain it", () => {
        const period: IPeriod = { id: 123 };
        const periodCollection: IPeriod[] = [{ id: 456 }];
        expectedResult = service.addPeriodToCollectionIfMissing(periodCollection, period);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(period);
      });

      it('should add only unique Period to an array', () => {
        const periodArray: IPeriod[] = [{ id: 123 }, { id: 456 }, { id: 77080 }];
        const periodCollection: IPeriod[] = [{ id: 123 }];
        expectedResult = service.addPeriodToCollectionIfMissing(periodCollection, ...periodArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const period: IPeriod = { id: 123 };
        const period2: IPeriod = { id: 456 };
        expectedResult = service.addPeriodToCollectionIfMissing([], period, period2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(period);
        expect(expectedResult).toContain(period2);
      });

      it('should accept null and undefined values', () => {
        const period: IPeriod = { id: 123 };
        expectedResult = service.addPeriodToCollectionIfMissing([], null, period, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(period);
      });

      it('should return initial array if no Period is added', () => {
        const periodCollection: IPeriod[] = [{ id: 123 }];
        expectedResult = service.addPeriodToCollectionIfMissing(periodCollection, undefined, null);
        expect(expectedResult).toEqual(periodCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
