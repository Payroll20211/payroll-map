import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISocialSecurity } from '../social-security.model';

@Component({
  selector: 'payroll-social-security-detail',
  templateUrl: './social-security-detail.component.html',
})
export class SocialSecurityDetailComponent implements OnInit {
  socialSecurity: ISocialSecurity | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ socialSecurity }) => {
      this.socialSecurity = socialSecurity;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
