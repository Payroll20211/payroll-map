import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProjectMaster, ProjectMaster } from '../project-master.model';

import { ProjectMasterService } from './project-master.service';

describe('ProjectMaster Service', () => {
  let service: ProjectMasterService;
  let httpMock: HttpTestingController;
  let elemDefault: IProjectMaster;
  let expectedResult: IProjectMaster | IProjectMaster[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProjectMasterService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      projectMasterCode: 'AAAAAAA',
      projectMasterName: 'AAAAAAA',
      costCenterType: 'AAAAAAA',
      projectDirectorName: 'AAAAAAA',
      phone: 'AAAAAAA',
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

    it('should create a ProjectMaster', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ProjectMaster()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProjectMaster', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          projectMasterCode: 'BBBBBB',
          projectMasterName: 'BBBBBB',
          costCenterType: 'BBBBBB',
          projectDirectorName: 'BBBBBB',
          phone: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProjectMaster', () => {
      const patchObject = Object.assign(
        {
          projectMasterCode: 'BBBBBB',
          projectDirectorName: 'BBBBBB',
          phone: 'BBBBBB',
        },
        new ProjectMaster()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProjectMaster', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          projectMasterCode: 'BBBBBB',
          projectMasterName: 'BBBBBB',
          costCenterType: 'BBBBBB',
          projectDirectorName: 'BBBBBB',
          phone: 'BBBBBB',
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

    it('should delete a ProjectMaster', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addProjectMasterToCollectionIfMissing', () => {
      it('should add a ProjectMaster to an empty array', () => {
        const projectMaster: IProjectMaster = { id: 123 };
        expectedResult = service.addProjectMasterToCollectionIfMissing([], projectMaster);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(projectMaster);
      });

      it('should not add a ProjectMaster to an array that contains it', () => {
        const projectMaster: IProjectMaster = { id: 123 };
        const projectMasterCollection: IProjectMaster[] = [
          {
            ...projectMaster,
          },
          { id: 456 },
        ];
        expectedResult = service.addProjectMasterToCollectionIfMissing(projectMasterCollection, projectMaster);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProjectMaster to an array that doesn't contain it", () => {
        const projectMaster: IProjectMaster = { id: 123 };
        const projectMasterCollection: IProjectMaster[] = [{ id: 456 }];
        expectedResult = service.addProjectMasterToCollectionIfMissing(projectMasterCollection, projectMaster);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(projectMaster);
      });

      it('should add only unique ProjectMaster to an array', () => {
        const projectMasterArray: IProjectMaster[] = [{ id: 123 }, { id: 456 }, { id: 317 }];
        const projectMasterCollection: IProjectMaster[] = [{ id: 123 }];
        expectedResult = service.addProjectMasterToCollectionIfMissing(projectMasterCollection, ...projectMasterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const projectMaster: IProjectMaster = { id: 123 };
        const projectMaster2: IProjectMaster = { id: 456 };
        expectedResult = service.addProjectMasterToCollectionIfMissing([], projectMaster, projectMaster2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(projectMaster);
        expect(expectedResult).toContain(projectMaster2);
      });

      it('should accept null and undefined values', () => {
        const projectMaster: IProjectMaster = { id: 123 };
        expectedResult = service.addProjectMasterToCollectionIfMissing([], null, projectMaster, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(projectMaster);
      });

      it('should return initial array if no ProjectMaster is added', () => {
        const projectMasterCollection: IProjectMaster[] = [{ id: 123 }];
        expectedResult = service.addProjectMasterToCollectionIfMissing(projectMasterCollection, undefined, null);
        expect(expectedResult).toEqual(projectMasterCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
