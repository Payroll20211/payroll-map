import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAllergy, Allergy } from '../allergy.model';

import { AllergyService } from './allergy.service';

describe('Allergy Service', () => {
  let service: AllergyService;
  let httpMock: HttpTestingController;
  let elemDefault: IAllergy;
  let expectedResult: IAllergy | IAllergy[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AllergyService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      description: 'AAAAAAA',
      treatment: 'AAAAAAA',
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

    it('should create a Allergy', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Allergy()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Allergy', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          treatment: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Allergy', () => {
      const patchObject = Object.assign({}, new Allergy());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Allergy', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          treatment: 'BBBBBB',
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

    it('should delete a Allergy', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAllergyToCollectionIfMissing', () => {
      it('should add a Allergy to an empty array', () => {
        const allergy: IAllergy = { id: 123 };
        expectedResult = service.addAllergyToCollectionIfMissing([], allergy);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(allergy);
      });

      it('should not add a Allergy to an array that contains it', () => {
        const allergy: IAllergy = { id: 123 };
        const allergyCollection: IAllergy[] = [
          {
            ...allergy,
          },
          { id: 456 },
        ];
        expectedResult = service.addAllergyToCollectionIfMissing(allergyCollection, allergy);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Allergy to an array that doesn't contain it", () => {
        const allergy: IAllergy = { id: 123 };
        const allergyCollection: IAllergy[] = [{ id: 456 }];
        expectedResult = service.addAllergyToCollectionIfMissing(allergyCollection, allergy);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(allergy);
      });

      it('should add only unique Allergy to an array', () => {
        const allergyArray: IAllergy[] = [{ id: 123 }, { id: 456 }, { id: 5515 }];
        const allergyCollection: IAllergy[] = [{ id: 123 }];
        expectedResult = service.addAllergyToCollectionIfMissing(allergyCollection, ...allergyArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const allergy: IAllergy = { id: 123 };
        const allergy2: IAllergy = { id: 456 };
        expectedResult = service.addAllergyToCollectionIfMissing([], allergy, allergy2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(allergy);
        expect(expectedResult).toContain(allergy2);
      });

      it('should accept null and undefined values', () => {
        const allergy: IAllergy = { id: 123 };
        expectedResult = service.addAllergyToCollectionIfMissing([], null, allergy, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(allergy);
      });

      it('should return initial array if no Allergy is added', () => {
        const allergyCollection: IAllergy[] = [{ id: 123 }];
        expectedResult = service.addAllergyToCollectionIfMissing(allergyCollection, undefined, null);
        expect(expectedResult).toEqual(allergyCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
