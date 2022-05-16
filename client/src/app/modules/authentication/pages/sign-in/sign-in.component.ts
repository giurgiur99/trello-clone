import { Component, OnInit } from "@angular/core";
import {
  FormBuilder,
  FormGroup,
  FormGroupDirective,
  Validators,
} from "@angular/forms";
import { Router } from "@angular/router";
import { CustomValidationService } from "src/app/core/services/custom-validation.service";
import { AuthenticationService } from "../../share/authentication.service";

@Component({
  selector: "app-sign-in",
  templateUrl: "./sign-in.component.html",
  styleUrls: ["./sign-in.component.scss"],
})
export class SignInComponent implements OnInit {
  isHide: boolean = true;
  signInForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private authenticationService: AuthenticationService,
    private customValidationService: CustomValidationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.createFormGroup();
  }

  createFormGroup() {
    this.signInForm = this.formBuilder.group({
      username: [
        "",
        Validators.compose([
          Validators.required,
          this.customValidationService.MinLengthAndMaxlengthValidator(4, 20),
          this.customValidationService.PatternAndMinLengthValidator(4),
        ]),
      ],
      password: [
        "",
        Validators.compose([
          Validators.required,
          this.customValidationService.MinLengthAndMaxlengthValidator(6, 30),
        ]),
      ],
    });
  }

  signIn(formDirective: FormGroupDirective) {
    this.authenticationService
      .signIn(this.signInForm.value)
      .subscribe((jwtResponse) => {
        formDirective.resetForm();
        this.signInForm.reset();
        console.log(jwtResponse.roles);
        if (jwtResponse.roles[0] === "ROLE_ADMIN") {
          console.log("admin here");
          this.router.navigate(["admin"]);
        } else
          this.router.navigate(["user/" + jwtResponse.username + "/boards"]);
      });
  }

  get getSignInForm() {
    return this.signInForm.controls;
  }

  signUpRoute() {
    this.router.navigate(["auth/signup"]);
  }

  toggleIsHide() {
    this.isHide = !this.isHide;
  }
}
