import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OperatorMatrizDetailComponent } from './operator-matriz-detail.component';

describe('OperatorMatriz Management Detail Component', () => {
  let comp: OperatorMatrizDetailComponent;
  let fixture: ComponentFixture<OperatorMatrizDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OperatorMatrizDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ operatorMatriz: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OperatorMatrizDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OperatorMatrizDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load operatorMatriz on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.operatorMatriz).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
