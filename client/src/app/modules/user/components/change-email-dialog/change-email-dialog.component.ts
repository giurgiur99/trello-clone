import { Component, HostListener, Inject, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { UserService } from 'src/app/modules/user/shared/user.service';
import { EmailRequest, UserProfileDetailResponse } from '../../interface/user.interface';

@Component({
  selector: 'app-change-email-dialog',
  templateUrl: './change-email-dialog.component.html',
  styleUrls: ['./change-email-dialog.component.scss']
})
export class ChangeEmailDialogComponent implements OnInit {

  emailRequest: EmailRequest = {} as EmailRequest;
  errorBackEnd: any = {};
  @ViewChild('emailForm') public emailForm: NgForm;

  constructor(
    public dialogRef: MatDialogRef<ChangeEmailDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public UserProfileDetailResponse: UserProfileDetailResponse,
    private userService: UserService) {
  }

  ngOnInit(): void {
  }

  clearErrorBackEndValidation() {
    if (this.errorBackEnd?.message) {
      this.errorBackEnd = {};
    }
  }

  emailValidator() {
    this.clearErrorBackEndValidation();
    const valid: boolean = this.emailForm.controls['email'].value === this.UserProfileDetailResponse.email;
    const email = this.emailForm.controls['email'];
    if (valid) {
      this.emailForm.controls['email'].setErrors({
        'mismatch': true
      })
    }
    else {
      email.setErrors(null);
      email.updateValueAndValidity();
    }
  }

  changeEmail() {
    this.userService.changeEmailById(this.UserProfileDetailResponse.id, this.emailRequest)
      .subscribe(result => {
        if (result) {
          this.close(result);
        }
      }, error => {
        this.errorBackEnd.message = error;
      })
  }

  confirm(): void {
    this.changeEmail();
  }

  cancel(): void {
    this.close(false);
  }

  close(value: any): void {
    this.dialogRef.close(value);
  }

  @HostListener("window:keyup.esc")
  onEsc() {
    this.close(false);
  }

}