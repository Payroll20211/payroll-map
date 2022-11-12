import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmployeeService } from '../service/employee.service';
import { IEmployee, Employee } from '../employee.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IContract } from 'app/entities/contract/contract.model';
import { ContractService } from 'app/entities/contract/service/contract.service';
import { IAllergy } from 'app/entities/allergy/allergy.model';
import { AllergyService } from 'app/entities/allergy/service/allergy.service';
import { ISocialPayments } from 'app/entities/social-payments/social-payments.model';
import { SocialPaymentsService } from 'app/entities/social-payments/service/social-payments.service';
import { IPositionArl } from 'app/entities/position-arl/position-arl.model';
import { PositionArlService } from 'app/entities/position-arl/service/position-arl.service';
import { IPeriod } from 'app/entities/period/period.model';
import { PeriodService } from 'app/entities/period/service/period.service';
import { IOperatorType } from 'app/entities/operator-type/operator-type.model';
import { OperatorTypeService } from 'app/entities/operator-type/service/operator-type.service';
import { IOperatorMatriz } from 'app/entities/operator-matriz/operator-matriz.model';
import { OperatorMatrizService } from 'app/entities/operator-matriz/service/operator-matriz.service';
import { ISocialSecurity } from 'app/entities/social-security/social-security.model';
import { SocialSecurityService } from 'app/entities/social-security/service/social-security.service';
import { IIncome } from 'app/entities/income/income.model';
import { IncomeService } from 'app/entities/income/service/income.service';
import { IDeduction } from 'app/entities/deduction/deduction.model';
import { DeductionService } from 'app/entities/deduction/service/deduction.service';
import { IDocumentType } from 'app/entities/document-type/document-type.model';
import { DocumentTypeService } from 'app/entities/document-type/service/document-type.service';

import { EmployeeUpdateComponent } from './employee-update.component';

describe('Employee Management Update Component', () => {
  let comp: EmployeeUpdateComponent;
  let fixture: ComponentFixture<EmployeeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employeeService: EmployeeService;
  let userService: UserService;
  let contractService: ContractService;
  let allergyService: AllergyService;
  let socialPaymentsService: SocialPaymentsService;
  let positionArlService: PositionArlService;
  let periodService: PeriodService;
  let operatorTypeService: OperatorTypeService;
  let operatorMatrizService: OperatorMatrizService;
  let socialSecurityService: SocialSecurityService;
  let incomeService: IncomeService;
  let deductionService: DeductionService;
  let documentTypeService: DocumentTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmployeeUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EmployeeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployeeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employeeService = TestBed.inject(EmployeeService);
    userService = TestBed.inject(UserService);
    contractService = TestBed.inject(ContractService);
    allergyService = TestBed.inject(AllergyService);
    socialPaymentsService = TestBed.inject(SocialPaymentsService);
    positionArlService = TestBed.inject(PositionArlService);
    periodService = TestBed.inject(PeriodService);
    operatorTypeService = TestBed.inject(OperatorTypeService);
    operatorMatrizService = TestBed.inject(OperatorMatrizService);
    socialSecurityService = TestBed.inject(SocialSecurityService);
    incomeService = TestBed.inject(IncomeService);
    deductionService = TestBed.inject(DeductionService);
    documentTypeService = TestBed.inject(DocumentTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const user: IUser = { id: 84478 };
      employee.user = user;

      const userCollection: IUser[] = [{ id: 1447 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call contract query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const contract: IContract = { id: 79958 };
      employee.contract = contract;

      const contractCollection: IContract[] = [{ id: 65336 }];
      jest.spyOn(contractService, 'query').mockReturnValue(of(new HttpResponse({ body: contractCollection })));
      const expectedCollection: IContract[] = [contract, ...contractCollection];
      jest.spyOn(contractService, 'addContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(contractService.query).toHaveBeenCalled();
      expect(contractService.addContractToCollectionIfMissing).toHaveBeenCalledWith(contractCollection, contract);
      expect(comp.contractsCollection).toEqual(expectedCollection);
    });

    it('Should call allergy query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const allergy: IAllergy = { id: 89296 };
      employee.allergy = allergy;

      const allergyCollection: IAllergy[] = [{ id: 96078 }];
      jest.spyOn(allergyService, 'query').mockReturnValue(of(new HttpResponse({ body: allergyCollection })));
      const expectedCollection: IAllergy[] = [allergy, ...allergyCollection];
      jest.spyOn(allergyService, 'addAllergyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(allergyService.query).toHaveBeenCalled();
      expect(allergyService.addAllergyToCollectionIfMissing).toHaveBeenCalledWith(allergyCollection, allergy);
      expect(comp.allergiesCollection).toEqual(expectedCollection);
    });

    it('Should call socialPayments query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const socialPayments: ISocialPayments = { id: 20973 };
      employee.socialPayments = socialPayments;

      const socialPaymentsCollection: ISocialPayments[] = [{ id: 74897 }];
      jest.spyOn(socialPaymentsService, 'query').mockReturnValue(of(new HttpResponse({ body: socialPaymentsCollection })));
      const expectedCollection: ISocialPayments[] = [socialPayments, ...socialPaymentsCollection];
      jest.spyOn(socialPaymentsService, 'addSocialPaymentsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(socialPaymentsService.query).toHaveBeenCalled();
      expect(socialPaymentsService.addSocialPaymentsToCollectionIfMissing).toHaveBeenCalledWith(socialPaymentsCollection, socialPayments);
      expect(comp.socialPaymentsCollection).toEqual(expectedCollection);
    });

    it('Should call positionArl query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const positionArl: IPositionArl = { id: 26723 };
      employee.positionArl = positionArl;

      const positionArlCollection: IPositionArl[] = [{ id: 23293 }];
      jest.spyOn(positionArlService, 'query').mockReturnValue(of(new HttpResponse({ body: positionArlCollection })));
      const expectedCollection: IPositionArl[] = [positionArl, ...positionArlCollection];
      jest.spyOn(positionArlService, 'addPositionArlToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(positionArlService.query).toHaveBeenCalled();
      expect(positionArlService.addPositionArlToCollectionIfMissing).toHaveBeenCalledWith(positionArlCollection, positionArl);
      expect(comp.positionArlsCollection).toEqual(expectedCollection);
    });

    it('Should call period query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const period: IPeriod = { id: 7389 };
      employee.period = period;

      const periodCollection: IPeriod[] = [{ id: 37801 }];
      jest.spyOn(periodService, 'query').mockReturnValue(of(new HttpResponse({ body: periodCollection })));
      const expectedCollection: IPeriod[] = [period, ...periodCollection];
      jest.spyOn(periodService, 'addPeriodToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(periodService.query).toHaveBeenCalled();
      expect(periodService.addPeriodToCollectionIfMissing).toHaveBeenCalledWith(periodCollection, period);
      expect(comp.periodsCollection).toEqual(expectedCollection);
    });

    it('Should call operatorType query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const operatorType: IOperatorType = { id: 30485 };
      employee.operatorType = operatorType;

      const operatorTypeCollection: IOperatorType[] = [{ id: 3525 }];
      jest.spyOn(operatorTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: operatorTypeCollection })));
      const expectedCollection: IOperatorType[] = [operatorType, ...operatorTypeCollection];
      jest.spyOn(operatorTypeService, 'addOperatorTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(operatorTypeService.query).toHaveBeenCalled();
      expect(operatorTypeService.addOperatorTypeToCollectionIfMissing).toHaveBeenCalledWith(operatorTypeCollection, operatorType);
      expect(comp.operatorTypesCollection).toEqual(expectedCollection);
    });

    it('Should call operatorMatriz query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const operatorMatriz: IOperatorMatriz = { id: 81881 };
      employee.operatorMatriz = operatorMatriz;

      const operatorMatrizCollection: IOperatorMatriz[] = [{ id: 75701 }];
      jest.spyOn(operatorMatrizService, 'query').mockReturnValue(of(new HttpResponse({ body: operatorMatrizCollection })));
      const expectedCollection: IOperatorMatriz[] = [operatorMatriz, ...operatorMatrizCollection];
      jest.spyOn(operatorMatrizService, 'addOperatorMatrizToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(operatorMatrizService.query).toHaveBeenCalled();
      expect(operatorMatrizService.addOperatorMatrizToCollectionIfMissing).toHaveBeenCalledWith(operatorMatrizCollection, operatorMatriz);
      expect(comp.operatorMatrizsCollection).toEqual(expectedCollection);
    });

    it('Should call socialSecurity query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const socialSecurity: ISocialSecurity = { id: 34960 };
      employee.socialSecurity = socialSecurity;

      const socialSecurityCollection: ISocialSecurity[] = [{ id: 40471 }];
      jest.spyOn(socialSecurityService, 'query').mockReturnValue(of(new HttpResponse({ body: socialSecurityCollection })));
      const expectedCollection: ISocialSecurity[] = [socialSecurity, ...socialSecurityCollection];
      jest.spyOn(socialSecurityService, 'addSocialSecurityToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(socialSecurityService.query).toHaveBeenCalled();
      expect(socialSecurityService.addSocialSecurityToCollectionIfMissing).toHaveBeenCalledWith(socialSecurityCollection, socialSecurity);
      expect(comp.socialSecuritiesCollection).toEqual(expectedCollection);
    });

    it('Should call Income query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const income: IIncome = { id: 73928 };
      employee.income = income;

      const incomeCollection: IIncome[] = [{ id: 79983 }];
      jest.spyOn(incomeService, 'query').mockReturnValue(of(new HttpResponse({ body: incomeCollection })));
      const additionalIncomes = [income];
      const expectedCollection: IIncome[] = [...additionalIncomes, ...incomeCollection];
      jest.spyOn(incomeService, 'addIncomeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(incomeService.query).toHaveBeenCalled();
      expect(incomeService.addIncomeToCollectionIfMissing).toHaveBeenCalledWith(incomeCollection, ...additionalIncomes);
      expect(comp.incomesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Deduction query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const deduction: IDeduction = { id: 16776 };
      employee.deduction = deduction;

      const deductionCollection: IDeduction[] = [{ id: 60751 }];
      jest.spyOn(deductionService, 'query').mockReturnValue(of(new HttpResponse({ body: deductionCollection })));
      const additionalDeductions = [deduction];
      const expectedCollection: IDeduction[] = [...additionalDeductions, ...deductionCollection];
      jest.spyOn(deductionService, 'addDeductionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(deductionService.query).toHaveBeenCalled();
      expect(deductionService.addDeductionToCollectionIfMissing).toHaveBeenCalledWith(deductionCollection, ...additionalDeductions);
      expect(comp.deductionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DocumentType query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const documentType: IDocumentType = { id: 42223 };
      employee.documentType = documentType;

      const documentTypeCollection: IDocumentType[] = [{ id: 61901 }];
      jest.spyOn(documentTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: documentTypeCollection })));
      const additionalDocumentTypes = [documentType];
      const expectedCollection: IDocumentType[] = [...additionalDocumentTypes, ...documentTypeCollection];
      jest.spyOn(documentTypeService, 'addDocumentTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(documentTypeService.query).toHaveBeenCalled();
      expect(documentTypeService.addDocumentTypeToCollectionIfMissing).toHaveBeenCalledWith(
        documentTypeCollection,
        ...additionalDocumentTypes
      );
      expect(comp.documentTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const employee: IEmployee = { id: 456 };
      const user: IUser = { id: 66698 };
      employee.user = user;
      const contract: IContract = { id: 28059 };
      employee.contract = contract;
      const allergy: IAllergy = { id: 30059 };
      employee.allergy = allergy;
      const socialPayments: ISocialPayments = { id: 30099 };
      employee.socialPayments = socialPayments;
      const positionArl: IPositionArl = { id: 89967 };
      employee.positionArl = positionArl;
      const period: IPeriod = { id: 95823 };
      employee.period = period;
      const operatorType: IOperatorType = { id: 57684 };
      employee.operatorType = operatorType;
      const operatorMatriz: IOperatorMatriz = { id: 72611 };
      employee.operatorMatriz = operatorMatriz;
      const socialSecurity: ISocialSecurity = { id: 67167 };
      employee.socialSecurity = socialSecurity;
      const income: IIncome = { id: 70771 };
      employee.income = income;
      const deduction: IDeduction = { id: 52890 };
      employee.deduction = deduction;
      const documentType: IDocumentType = { id: 58219 };
      employee.documentType = documentType;

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(employee));
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.contractsCollection).toContain(contract);
      expect(comp.allergiesCollection).toContain(allergy);
      expect(comp.socialPaymentsCollection).toContain(socialPayments);
      expect(comp.positionArlsCollection).toContain(positionArl);
      expect(comp.periodsCollection).toContain(period);
      expect(comp.operatorTypesCollection).toContain(operatorType);
      expect(comp.operatorMatrizsCollection).toContain(operatorMatriz);
      expect(comp.socialSecuritiesCollection).toContain(socialSecurity);
      expect(comp.incomesSharedCollection).toContain(income);
      expect(comp.deductionsSharedCollection).toContain(deduction);
      expect(comp.documentTypesSharedCollection).toContain(documentType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Employee>>();
      const employee = { id: 123 };
      jest.spyOn(employeeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employee }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(employeeService.update).toHaveBeenCalledWith(employee);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Employee>>();
      const employee = new Employee();
      jest.spyOn(employeeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employee }));
      saveSubject.complete();

      // THEN
      expect(employeeService.create).toHaveBeenCalledWith(employee);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Employee>>();
      const employee = { id: 123 };
      jest.spyOn(employeeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employeeService.update).toHaveBeenCalledWith(employee);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackContractById', () => {
      it('Should return tracked Contract primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackContractById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackAllergyById', () => {
      it('Should return tracked Allergy primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAllergyById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSocialPaymentsById', () => {
      it('Should return tracked SocialPayments primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSocialPaymentsById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPositionArlById', () => {
      it('Should return tracked PositionArl primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPositionArlById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPeriodById', () => {
      it('Should return tracked Period primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPeriodById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackOperatorTypeById', () => {
      it('Should return tracked OperatorType primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackOperatorTypeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackOperatorMatrizById', () => {
      it('Should return tracked OperatorMatriz primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackOperatorMatrizById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSocialSecurityById', () => {
      it('Should return tracked SocialSecurity primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSocialSecurityById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackIncomeById', () => {
      it('Should return tracked Income primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackIncomeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDeductionById', () => {
      it('Should return tracked Deduction primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDeductionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDocumentTypeById', () => {
      it('Should return tracked DocumentType primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDocumentTypeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
