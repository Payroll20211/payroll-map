import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IOperatorType, OperatorType } from '../operator-type.model';
import { OperatorTypeService } from '../service/operator-type.service';

import { OperatorTypeRoutingResolveService } from './operator-type-routing-resolve.service';

describe('OperatorType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: OperatorTypeRoutingResolveService;
  let service: OperatorTypeService;
  let resultOperatorType: IOperatorType | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(OperatorTypeRoutingResolveService);
    service = TestBed.inject(OperatorTypeService);
    resultOperatorType = undefined;
  });

  describe('resolve', () => {
    it('should return IOperatorType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOperatorType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultOperatorType).toEqual({ id: 123 });
    });

    it('should return new IOperatorType if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOperatorType = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultOperatorType).toEqual(new OperatorType());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as OperatorType })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOperatorType = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultOperatorType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
