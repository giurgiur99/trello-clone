import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CustomValidationService } from 'src/app/core/services/custom-validation.service';
import { NotificationService } from 'src/app/core/services/notification.service';
import { AuthenticationService } from '../../share/authentication.service';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.scss']
})
export class SignUpComponent implements OnInit {

  signUpForm: FormGroup;

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private authenticationService: AuthenticationService,
    private notificationService: NotificationService,
    private customValidation: CustomValidationService
  ) {

  }

  ngOnInit(): void {
    this.createFormGroup();
  }

  signUp() {
    this.authenticationService.
      signUp(this.signUpForm.value).subscribe(apiResponse => {
        this.signUpForm.reset();
        this.notificationService.open(apiResponse.message);
        this.router.navigate(['auth/signin']);
      },error => {
        this.notificationService.open(error, 'failed');
      })
  }

  createFormGroup() {
    this.signUpForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.maxLength(40)]],
      username: ['',
        Validators.compose([Validators.required,
        this.customValidation.MinLengthAndMaxlengthValidator(4, 20),
        this.customValidation.PatternAndMinLengthValidator(4)],
        )],
      password: ['',
        Validators.compose([Validators.required,
        this.customValidation.MinLengthAndMaxlengthValidator(6, 30),
        this.customValidation.PatternAndMinLengthValidator(6)])],
      email: ['',
        Validators.compose([Validators.required,
        Validators.maxLength(40),
        Validators.email])]
    })
  }

  goToSignIn() {
    this.router.navigate(['auth/signin']);
  }

  get getSignUpForm() {
    return this.signUpForm.controls;
  }

}