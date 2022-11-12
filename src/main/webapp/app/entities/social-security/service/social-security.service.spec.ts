import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISocialSecurity, SocialSecurity } from '../social-security.model';

import { SocialSecurityService } from './social-security.service';

describe('SocialSecurity Service', () => {
  let service: SocialSecurityService;
  let httpMock: HttpTestingController;
  let elemDefault: ISocialSecurity;
  let expectedResult: ISocialSecurity | ISocialSecurity[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SocialSecurityService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      eps: 'AAAAAAA',
      afp: 'AAAAAAA',
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

    it('should create a SocialSecurity', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SocialSecurity()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SocialSecurity', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          eps: 'BBBBBB',
          afp: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SocialSecurity', () => {
      const patchObject = Object.assign(
        {
          afp: 'BBBBBB',
        },
        new SocialSecurity()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SocialSecurity', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          eps: 'BBBBBB',
          afp: 'BBBBBB',
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

    it('should delete a SocialSecurity', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSocialSecurityToCollectionIfMissing', () => {
      it('should add a SocialSecurity to an empty array', () => {
        const socialSecurity: ISocialSecurity = { id: 123 };
        expectedResult = service.addSocialSecurityToCollectionIfMissing([], socialSecurity);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(socialSecurity);
      });

      it('should not add a SocialSecurity to an array that contains it', () => {
        const socialSecurity: ISocialSecurity = { id: 123 };
        const socialSecurityCollection: ISocialSecurity[] = [
          {
            ...socialSecurity,
          },
          { id: 456 },
        ];
        expectedResult = service.addSocialSecurityToCollectionIfMissing(socialSecurityCollection, socialSecurity);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SocialSecurity to an array that doesn't contain it", () => {
        const socialSecurity: ISocialSecurity = { id: 123 };
        const socialSecurityCollection: ISocialSecurity[] = [{ id: 456 }];
        expectedResult = service.addSocialSecurityToCollectionIfMissing(socialSecurityCollection, socialSecurity);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(socialSecurity);
      });

      it('should add only unique SocialSecurity to an array', () => {
        const socialSecurityArray: ISocialSecurity[] = [{ id: 123 }, { id: 456 }, { id: 18776 }];
        const socialSecurityCollection: ISocialSecurity[] = [{ id: 123 }];
        expectedResult = service.addSocialSecurityToCollectionIfMissing(socialSecurityCollection, ...socialSecurityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const socialSecurity: ISocialSecurity = { id: 123 };
        const socialSecurity2: ISocialSecurity = { id: 456 };
        expectedResult = service.addSocialSecurityToCollectionIfMissing([], socialSecurity, socialSecurity2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(socialSecurity);
        expect(expectedResult).toContain(socialSecurity2);
      });

      it('should accept null and undefined values', () => {
        const socialSecurity: ISocialSecurity = { id: 123 };
        expectedResult = service.addSocialSecurityToCollectionIfMissing([], null, socialSecurity, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(socialSecurity);
      });

      it('should return initial array if no SocialSecurity is added', () => {
        const socialSecurityCollection: ISocialSecurity[] = [{ id: 123 }];
        expectedResult = service.addSocialSecurityToCollectionIfMissing(socialSecurityCollection, undefined, null);
        expect(expectedResult).toEqual(socialSecurityCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
