import { Component, HostListener, Inject, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { UserService } from 'src/app/modules/user/shared/user.service';
import { PasswordRequest, UserProfileDetailResponse } from '../../interface/user.interface';

@Component({
  selector: 'app-change-password-dialog',
  templateUrl: './change-password-dialog.component.html',
  styleUrls: ['./change-password-dialog.component.scss']
})
export class ChangePasswordDialogComponent implements OnInit {

  passwordRequest: PasswordRequest = {} as PasswordRequest;
  errorBackEnd: any = {};
  @ViewChild("passwordForm") public passwordForm: NgForm;

  constructor(
    public dialogRef: MatDialogRef<ChangePasswordDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public UserProfileDetailResponse: UserProfileDetailResponse,
    private userService: UserService
  ) { }

  ngOnInit(): void {
  }

  clearErrorBackEndValidation() {
    if (this.errorBackEnd?.message) {
      this.errorBackEnd = {};
    }
  }

  confirmPasswordValidator() {
    this.clearErrorBackEndValidation();
    const valid: boolean =
      this.passwordForm.controls['newPassword'].value ===
      this.passwordForm.controls['confirmPassword'].value;

    const confirmPassword = this.passwordForm.controls['confirmPassword'];
    if (valid) {
      confirmPassword.setErrors(null);
    } else {
      confirmPassword.setErrors({
        'mismatch': true
      })
    }
  }

  changePassword() {
    this.userService.changePasswordById(
      this.UserProfileDetailResponse.id,
      this.passwordRequest).subscribe(result => {
        if (result) {
          this.close(result.message)
        }
      }, error => {
        console.log(error);
        this.errorBackEnd.message = error;
      })
  }

  confirm(): void {
    this.changePassword();
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
