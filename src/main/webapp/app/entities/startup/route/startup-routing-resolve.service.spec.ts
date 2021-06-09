jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IStartup, Startup } from '../startup.model';
import { StartupService } from '../service/startup.service';

import { StartupRoutingResolveService } from './startup-routing-resolve.service';

describe('Service Tests', () => {
  describe('Startup routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: StartupRoutingResolveService;
    let service: StartupService;
    let resultStartup: IStartup | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(StartupRoutingResolveService);
      service = TestBed.inject(StartupService);
      resultStartup = undefined;
    });

    describe('resolve', () => {
      it('should return IStartup returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStartup = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultStartup).toEqual({ id: 123 });
      });

      it('should return new IStartup if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStartup = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultStartup).toEqual(new Startup());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultStartup = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultStartup).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
