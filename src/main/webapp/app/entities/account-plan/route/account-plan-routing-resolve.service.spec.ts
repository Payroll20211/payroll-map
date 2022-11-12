import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IAccountPlan, AccountPlan } from '../account-plan.model';
import { AccountPlanService } from '../service/account-plan.service';

import { AccountPlanRoutingResolveService } from './account-plan-routing-resolve.service';

describe('AccountPlan routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AccountPlanRoutingResolveService;
  let service: AccountPlanService;
  let resultAccountPlan: IAccountPlan | undefined;

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
    routingResolveService = TestBed.inject(AccountPlanRoutingResolveService);
    service = TestBed.inject(AccountPlanService);
    resultAccountPlan = undefined;
  });

  describe('resolve', () => {
    it('should return IAccountPlan returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAccountPlan = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAccountPlan).toEqual({ id: 123 });
    });

    it('should return new IAccountPlan if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAccountPlan = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAccountPlan).toEqual(new AccountPlan());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AccountPlan })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAccountPlan = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAccountPlan).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
