import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISocialPayments, SocialPayments } from '../social-payments.model';

import { SocialPaymentsService } from './social-payments.service';

describe('SocialPayments Service', () => {
  let service: SocialPaymentsService;
  let httpMock: HttpTestingController;
  let elemDefault: ISocialPayments;
  let expectedResult: ISocialPayments | ISocialPayments[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SocialPaymentsService);
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

    it('should create a SocialPayments', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SocialPayments()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SocialPayments', () => {
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

    it('should partial update a SocialPayments', () => {
      const patchObject = Object.assign(
        {
          description: 'BBBBBB',
        },
        new SocialPayments()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SocialPayments', () => {
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

    it('should delete a SocialPayments', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSocialPaymentsToCollectionIfMissing', () => {
      it('should add a SocialPayments to an empty array', () => {
        const socialPayments: ISocialPayments = { id: 123 };
        expectedResult = service.addSocialPaymentsToCollectionIfMissing([], socialPayments);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(socialPayments);
      });

      it('should not add a SocialPayments to an array that contains it', () => {
        const socialPayments: ISocialPayments = { id: 123 };
        const socialPaymentsCollection: ISocialPayments[] = [
          {
            ...socialPayments,
          },
          { id: 456 },
        ];
        expectedResult = service.addSocialPaymentsToCollectionIfMissing(socialPaymentsCollection, socialPayments);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SocialPayments to an array that doesn't contain it", () => {
        const socialPayments: ISocialPayments = { id: 123 };
        const socialPaymentsCollection: ISocialPayments[] = [{ id: 456 }];
        expectedResult = service.addSocialPaymentsToCollectionIfMissing(socialPaymentsCollection, socialPayments);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(socialPayments);
      });

      it('should add only unique SocialPayments to an array', () => {
        const socialPaymentsArray: ISocialPayments[] = [{ id: 123 }, { id: 456 }, { id: 54794 }];
        const socialPaymentsCollection: ISocialPayments[] = [{ id: 123 }];
        expectedResult = service.addSocialPaymentsToCollectionIfMissing(socialPaymentsCollection, ...socialPaymentsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const socialPayments: ISocialPayments = { id: 123 };
        const socialPayments2: ISocialPayments = { id: 456 };
        expectedResult = service.addSocialPaymentsToCollectionIfMissing([], socialPayments, socialPayments2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(socialPayments);
        expect(expectedResult).toContain(socialPayments2);
      });

      it('should accept null and undefined values', () => {
        const socialPayments: ISocialPayments = { id: 123 };
        expectedResult = service.addSocialPaymentsToCollectionIfMissing([], null, socialPayments, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(socialPayments);
      });

      it('should return initial array if no SocialPayments is added', () => {
        const socialPaymentsCollection: ISocialPayments[] = [{ id: 123 }];
        expectedResult = service.addSocialPaymentsToCollectionIfMissing(socialPaymentsCollection, undefined, null);
        expect(expectedResult).toEqual(socialPaymentsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
