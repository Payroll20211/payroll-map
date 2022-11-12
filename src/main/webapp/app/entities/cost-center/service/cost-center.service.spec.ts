import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICostCenter, CostCenter } from '../cost-center.model';

import { CostCenterService } from './cost-center.service';

describe('CostCenter Service', () => {
  let service: CostCenterService;
  let httpMock: HttpTestingController;
  let elemDefault: ICostCenter;
  let expectedResult: ICostCenter | ICostCenter[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CostCenterService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      costCenterCode: 'AAAAAAA',
      costCenterName: 'AAAAAAA',
      costCenterType: 'AAAAAAA',
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

    it('should create a CostCenter', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CostCenter()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CostCenter', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          costCenterCode: 'BBBBBB',
          costCenterName: 'BBBBBB',
          costCenterType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CostCenter', () => {
      const patchObject = Object.assign(
        {
          costCenterCode: 'BBBBBB',
          costCenterName: 'BBBBBB',
        },
        new CostCenter()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CostCenter', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          costCenterCode: 'BBBBBB',
          costCenterName: 'BBBBBB',
          costCenterType: 'BBBBBB',
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

    it('should delete a CostCenter', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCostCenterToCollectionIfMissing', () => {
      it('should add a CostCenter to an empty array', () => {
        const costCenter: ICostCenter = { id: 123 };
        expectedResult = service.addCostCenterToCollectionIfMissing([], costCenter);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(costCenter);
      });

      it('should not add a CostCenter to an array that contains it', () => {
        const costCenter: ICostCenter = { id: 123 };
        const costCenterCollection: ICostCenter[] = [
          {
            ...costCenter,
          },
          { id: 456 },
        ];
        expectedResult = service.addCostCenterToCollectionIfMissing(costCenterCollection, costCenter);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CostCenter to an array that doesn't contain it", () => {
        const costCenter: ICostCenter = { id: 123 };
        const costCenterCollection: ICostCenter[] = [{ id: 456 }];
        expectedResult = service.addCostCenterToCollectionIfMissing(costCenterCollection, costCenter);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(costCenter);
      });

      it('should add only unique CostCenter to an array', () => {
        const costCenterArray: ICostCenter[] = [{ id: 123 }, { id: 456 }, { id: 94799 }];
        const costCenterCollection: ICostCenter[] = [{ id: 123 }];
        expectedResult = service.addCostCenterToCollectionIfMissing(costCenterCollection, ...costCenterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const costCenter: ICostCenter = { id: 123 };
        const costCenter2: ICostCenter = { id: 456 };
        expectedResult = service.addCostCenterToCollectionIfMissing([], costCenter, costCenter2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(costCenter);
        expect(expectedResult).toContain(costCenter2);
      });

      it('should accept null and undefined values', () => {
        const costCenter: ICostCenter = { id: 123 };
        expectedResult = service.addCostCenterToCollectionIfMissing([], null, costCenter, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(costCenter);
      });

      it('should return initial array if no CostCenter is added', () => {
        const costCenterCollection: ICostCenter[] = [{ id: 123 }];
        expectedResult = service.addCostCenterToCollectionIfMissing(costCenterCollection, undefined, null);
        expect(expectedResult).toEqual(costCenterCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
