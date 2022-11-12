import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IEmployee, Employee } from '../employee.model';
import { EmployeeService } from '../service/employee.service';
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
import { StateEmployee } from 'app/entities/enumerations/state-employee.model';

@Component({
  selector: 'payroll-employee-update',
  templateUrl: './employee-update.component.html',
})
export class EmployeeUpdateComponent implements OnInit {
  isSaving = false;
  stateEmployeeValues = Object.keys(StateEmployee);

  usersSharedCollection: IUser[] = [];
  contractsCollection: IContract[] = [];
  allergiesCollection: IAllergy[] = [];
  socialPaymentsCollection: ISocialPayments[] = [];
  positionArlsCollection: IPositionArl[] = [];
  periodsCollection: IPeriod[] = [];
  operatorTypesCollection: IOperatorType[] = [];
  operatorMatrizsCollection: IOperatorMatriz[] = [];
  socialSecuritiesCollection: ISocialSecurity[] = [];
  incomesSharedCollection: IIncome[] = [];
  deductionsSharedCollection: IDeduction[] = [];
  documentTypesSharedCollection: IDocumentType[] = [];

  editForm = this.fb.group({
    id: [],
    completeName: [null, [Validators.required, Validators.maxLength(100)]],
    address: [null, [Validators.required, Validators.maxLength(100)]],
    dateStart: [null, [Validators.required]],
    city: [null, [Validators.required, Validators.maxLength(50)]],
    mobile: [null, [Validators.required]],
    stateEmployee: [null, [Validators.required]],
    user: [null, Validators.required],
    contract: [null, Validators.required],
    allergy: [null, Validators.required],
    socialPayments: [null, Validators.required],
    positionArl: [null, Validators.required],
    period: [null, Validators.required],
    operatorType: [null, Validators.required],
    operatorMatriz: [null, Validators.required],
    socialSecurity: [null, Validators.required],
    income: [],
    deduction: [],
    documentType: [],
  });

  constructor(
    protected employeeService: EmployeeService,
    protected userService: UserService,
    protected contractService: ContractService,
    protected allergyService: AllergyService,
    protected socialPaymentsService: SocialPaymentsService,
    protected positionArlService: PositionArlService,
    protected periodService: PeriodService,
    protected operatorTypeService: OperatorTypeService,
    protected operatorMatrizService: OperatorMatrizService,
    protected socialSecurityService: SocialSecurityService,
    protected incomeService: IncomeService,
    protected deductionService: DeductionService,
    protected documentTypeService: DocumentTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employee }) => {
      if (employee.id === undefined) {
        const today = dayjs().startOf('day');
        employee.dateStart = today;
      }

      this.updateForm(employee);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employee = this.createFromForm();
    if (employee.id !== undefined) {
      this.subscribeToSaveResponse(this.employeeService.update(employee));
    } else {
      this.subscribeToSaveResponse(this.employeeService.create(employee));
    }
  }

  trackUserById(_index: number, item: IUser): number {
    return item.id!;
  }

  trackContractById(_index: number, item: IContract): number {
    return item.id!;
  }

  trackAllergyById(_index: number, item: IAllergy): number {
    return item.id!;
  }

  trackSocialPaymentsById(_index: number, item: ISocialPayments): number {
    return item.id!;
  }

  trackPositionArlById(_index: number, item: IPositionArl): number {
    return item.id!;
  }

  trackPeriodById(_index: number, item: IPeriod): number {
    return item.id!;
  }

  trackOperatorTypeById(_index: number, item: IOperatorType): number {
    return item.id!;
  }

  trackOperatorMatrizById(_index: number, item: IOperatorMatriz): number {
    return item.id!;
  }

  trackSocialSecurityById(_index: number, item: ISocialSecurity): number {
    return item.id!;
  }

  trackIncomeById(_index: number, item: IIncome): number {
    return item.id!;
  }

  trackDeductionById(_index: number, item: IDeduction): number {
    return item.id!;
  }

  trackDocumentTypeById(_index: number, item: IDocumentType): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployee>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(employee: IEmployee): void {
    this.editForm.patchValue({
      id: employee.id,
      completeName: employee.completeName,
      address: employee.address,
      dateStart: employee.dateStart ? employee.dateStart.format(DATE_TIME_FORMAT) : null,
      city: employee.city,
      mobile: employee.mobile,
      stateEmployee: employee.stateEmployee,
      user: employee.user,
      contract: employee.contract,
      allergy: employee.allergy,
      socialPayments: employee.socialPayments,
      positionArl: employee.positionArl,
      period: employee.period,
      operatorType: employee.operatorType,
      operatorMatriz: employee.operatorMatriz,
      socialSecurity: employee.socialSecurity,
      income: employee.income,
      deduction: employee.deduction,
      documentType: employee.documentType,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, employee.user);
    this.contractsCollection = this.contractService.addContractToCollectionIfMissing(this.contractsCollection, employee.contract);
    this.allergiesCollection = this.allergyService.addAllergyToCollectionIfMissing(this.allergiesCollection, employee.allergy);
    this.socialPaymentsCollection = this.socialPaymentsService.addSocialPaymentsToCollectionIfMissing(
      this.socialPaymentsCollection,
      employee.socialPayments
    );
    this.positionArlsCollection = this.positionArlService.addPositionArlToCollectionIfMissing(
      this.positionArlsCollection,
      employee.positionArl
    );
    this.periodsCollection = this.periodService.addPeriodToCollectionIfMissing(this.periodsCollection, employee.period);
    this.operatorTypesCollection = this.operatorTypeService.addOperatorTypeToCollectionIfMissing(
      this.operatorTypesCollection,
      employee.operatorType
    );
    this.operatorMatrizsCollection = this.operatorMatrizService.addOperatorMatrizToCollectionIfMissing(
      this.operatorMatrizsCollection,
      employee.operatorMatriz
    );
    this.socialSecuritiesCollection = this.socialSecurityService.addSocialSecurityToCollectionIfMissing(
      this.socialSecuritiesCollection,
      employee.socialSecurity
    );
    this.incomesSharedCollection = this.incomeService.addIncomeToCollectionIfMissing(this.incomesSharedCollection, employee.income);
    this.deductionsSharedCollection = this.deductionService.addDeductionToCollectionIfMissing(
      this.deductionsSharedCollection,
      employee.deduction
    );
    this.documentTypesSharedCollection = this.documentTypeService.addDocumentTypeToCollectionIfMissing(
      this.documentTypesSharedCollection,
      employee.documentType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.contractService
      .query({ filter: 'employee-is-null' })
      .pipe(map((res: HttpResponse<IContract[]>) => res.body ?? []))
      .pipe(
        map((contracts: IContract[]) =>
          this.contractService.addContractToCollectionIfMissing(contracts, this.editForm.get('contract')!.value)
        )
      )
      .subscribe((contracts: IContract[]) => (this.contractsCollection = contracts));

    this.allergyService
      .query({ filter: 'employee-is-null' })
      .pipe(map((res: HttpResponse<IAllergy[]>) => res.body ?? []))
      .pipe(
        map((allergies: IAllergy[]) => this.allergyService.addAllergyToCollectionIfMissing(allergies, this.editForm.get('allergy')!.value))
      )
      .subscribe((allergies: IAllergy[]) => (this.allergiesCollection = allergies));

    this.socialPaymentsService
      .query({ filter: 'employee-is-null' })
      .pipe(map((res: HttpResponse<ISocialPayments[]>) => res.body ?? []))
      .pipe(
        map((socialPayments: ISocialPayments[]) =>
          this.socialPaymentsService.addSocialPaymentsToCollectionIfMissing(socialPayments, this.editForm.get('socialPayments')!.value)
        )
      )
      .subscribe((socialPayments: ISocialPayments[]) => (this.socialPaymentsCollection = socialPayments));

    this.positionArlService
      .query({ filter: 'employee-is-null' })
      .pipe(map((res: HttpResponse<IPositionArl[]>) => res.body ?? []))
      .pipe(
        map((positionArls: IPositionArl[]) =>
          this.positionArlService.addPositionArlToCollectionIfMissing(positionArls, this.editForm.get('positionArl')!.value)
        )
      )
      .subscribe((positionArls: IPositionArl[]) => (this.positionArlsCollection = positionArls));

    this.periodService
      .query({ filter: 'employee-is-null' })
      .pipe(map((res: HttpResponse<IPeriod[]>) => res.body ?? []))
      .pipe(map((periods: IPeriod[]) => this.periodService.addPeriodToCollectionIfMissing(periods, this.editForm.get('period')!.value)))
      .subscribe((periods: IPeriod[]) => (this.periodsCollection = periods));

    this.operatorTypeService
      .query({ filter: 'employee-is-null' })
      .pipe(map((res: HttpResponse<IOperatorType[]>) => res.body ?? []))
      .pipe(
        map((operatorTypes: IOperatorType[]) =>
          this.operatorTypeService.addOperatorTypeToCollectionIfMissing(operatorTypes, this.editForm.get('operatorType')!.value)
        )
      )
      .subscribe((operatorTypes: IOperatorType[]) => (this.operatorTypesCollection = operatorTypes));

    this.operatorMatrizService
      .query({ filter: 'employee-is-null' })
      .pipe(map((res: HttpResponse<IOperatorMatriz[]>) => res.body ?? []))
      .pipe(
        map((operatorMatrizs: IOperatorMatriz[]) =>
          this.operatorMatrizService.addOperatorMatrizToCollectionIfMissing(operatorMatrizs, this.editForm.get('operatorMatriz')!.value)
        )
      )
      .subscribe((operatorMatrizs: IOperatorMatriz[]) => (this.operatorMatrizsCollection = operatorMatrizs));

    this.socialSecurityService
      .query({ filter: 'employee-is-null' })
      .pipe(map((res: HttpResponse<ISocialSecurity[]>) => res.body ?? []))
      .pipe(
        map((socialSecurities: ISocialSecurity[]) =>
          this.socialSecurityService.addSocialSecurityToCollectionIfMissing(socialSecurities, this.editForm.get('socialSecurity')!.value)
        )
      )
      .subscribe((socialSecurities: ISocialSecurity[]) => (this.socialSecuritiesCollection = socialSecurities));

    this.incomeService
      .query()
      .pipe(map((res: HttpResponse<IIncome[]>) => res.body ?? []))
      .pipe(map((incomes: IIncome[]) => this.incomeService.addIncomeToCollectionIfMissing(incomes, this.editForm.get('income')!.value)))
      .subscribe((incomes: IIncome[]) => (this.incomesSharedCollection = incomes));

    this.deductionService
      .query()
      .pipe(map((res: HttpResponse<IDeduction[]>) => res.body ?? []))
      .pipe(
        map((deductions: IDeduction[]) =>
          this.deductionService.addDeductionToCollectionIfMissing(deductions, this.editForm.get('deduction')!.value)
        )
      )
      .subscribe((deductions: IDeduction[]) => (this.deductionsSharedCollection = deductions));

    this.documentTypeService
      .query()
      .pipe(map((res: HttpResponse<IDocumentType[]>) => res.body ?? []))
      .pipe(
        map((documentTypes: IDocumentType[]) =>
          this.documentTypeService.addDocumentTypeToCollectionIfMissing(documentTypes, this.editForm.get('documentType')!.value)
        )
      )
      .subscribe((documentTypes: IDocumentType[]) => (this.documentTypesSharedCollection = documentTypes));
  }

  protected createFromForm(): IEmployee {
    return {
      ...new Employee(),
      id: this.editForm.get(['id'])!.value,
      completeName: this.editForm.get(['completeName'])!.value,
      address: this.editForm.get(['address'])!.value,
      dateStart: this.editForm.get(['dateStart'])!.value ? dayjs(this.editForm.get(['dateStart'])!.value, DATE_TIME_FORMAT) : undefined,
      city: this.editForm.get(['city'])!.value,
      mobile: this.editForm.get(['mobile'])!.value,
      stateEmployee: this.editForm.get(['stateEmployee'])!.value,
      user: this.editForm.get(['user'])!.value,
      contract: this.editForm.get(['contract'])!.value,
      allergy: this.editForm.get(['allergy'])!.value,
      socialPayments: this.editForm.get(['socialPayments'])!.value,
      positionArl: this.editForm.get(['positionArl'])!.value,
      period: this.editForm.get(['period'])!.value,
      operatorType: this.editForm.get(['operatorType'])!.value,
      operatorMatriz: this.editForm.get(['operatorMatriz'])!.value,
      socialSecurity: this.editForm.get(['socialSecurity'])!.value,
      income: this.editForm.get(['income'])!.value,
      deduction: this.editForm.get(['deduction'])!.value,
      documentType: this.editForm.get(['documentType'])!.value,
    };
  }
}
