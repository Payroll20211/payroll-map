import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAccountPlan, AccountPlan } from '../account-plan.model';

import { AccountPlanService } from './account-plan.service';

describe('AccountPlan Service', () => {
  let service: AccountPlanService;
  let httpMock: HttpTestingController;
  let elemDefault: IAccountPlan;
  let expectedResult: IAccountPlan | IAccountPlan[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AccountPlanService);
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

    it('should create a AccountPlan', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new AccountPlan()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AccountPlan', () => {
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

    it('should partial update a AccountPlan', () => {
      const patchObject = Object.assign(
        {
          code: 'BBBBBB',
          description: 'BBBBBB',
        },
        new AccountPlan()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AccountPlan', () => {
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

    it('should delete a AccountPlan', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAccountPlanToCollectionIfMissing', () => {
      it('should add a AccountPlan to an empty array', () => {
        const accountPlan: IAccountPlan = { id: 123 };
        expectedResult = service.addAccountPlanToCollectionIfMissing([], accountPlan);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountPlan);
      });

      it('should not add a AccountPlan to an array that contains it', () => {
        const accountPlan: IAccountPlan = { id: 123 };
        const accountPlanCollection: IAccountPlan[] = [
          {
            ...accountPlan,
          },
          { id: 456 },
        ];
        expectedResult = service.addAccountPlanToCollectionIfMissing(accountPlanCollection, accountPlan);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AccountPlan to an array that doesn't contain it", () => {
        const accountPlan: IAccountPlan = { id: 123 };
        const accountPlanCollection: IAccountPlan[] = [{ id: 456 }];
        expectedResult = service.addAccountPlanToCollectionIfMissing(accountPlanCollection, accountPlan);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountPlan);
      });

      it('should add only unique AccountPlan to an array', () => {
        const accountPlanArray: IAccountPlan[] = [{ id: 123 }, { id: 456 }, { id: 41711 }];
        const accountPlanCollection: IAccountPlan[] = [{ id: 123 }];
        expectedResult = service.addAccountPlanToCollectionIfMissing(accountPlanCollection, ...accountPlanArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const accountPlan: IAccountPlan = { id: 123 };
        const accountPlan2: IAccountPlan = { id: 456 };
        expectedResult = service.addAccountPlanToCollectionIfMissing([], accountPlan, accountPlan2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountPlan);
        expect(expectedResult).toContain(accountPlan2);
      });

      it('should accept null and undefined values', () => {
        const accountPlan: IAccountPlan = { id: 123 };
        expectedResult = service.addAccountPlanToCollectionIfMissing([], null, accountPlan, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountPlan);
      });

      it('should return initial array if no AccountPlan is added', () => {
        const accountPlanCollection: IAccountPlan[] = [{ id: 123 }];
        expectedResult = service.addAccountPlanToCollectionIfMissing(accountPlanCollection, undefined, null);
        expect(expectedResult).toEqual(accountPlanCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
