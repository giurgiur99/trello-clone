import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { FileUploader } from 'ng2-file-upload';
import { ConfirmDialogRequest } from 'src/app/core/interface/core.interface';
import { NotificationService } from 'src/app/core/services/notification.service';
import { UserService } from 'src/app/modules/user/shared/user.service';
import { ConfirmDialogComponent } from 'src/app/shared/component/confirm-dialog/confirm-dialog.component';
import { FormNameDialogService } from 'src/app/shared/component/form-name-dialog/form-name-dialog.service';
import { UserProfileDetailResponse } from '../../interface/user.interface';
import { ChangeEmailDialogService } from '../../shared/change-email-dialog.service';
import { ChangePasswordDialogService } from '../../shared/change-password-dialog.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent implements OnInit {
  UserProfileDetailResponse: UserProfileDetailResponse = {
    id: '',
    name: '',
    username: '',
    email: '',
    joinedAt: undefined,
  };

  public uploader: FileUploader;

  constructor(
    private userService: UserService,
    private dialog: MatDialog,
    private changePasswordDialogService: ChangePasswordDialogService,
    private changeEmailDialogService: ChangeEmailDialogService,
    private formNameDialogService: FormNameDialogService,
    private notificationService: NotificationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const maxFileSize5MB = 5 * 1024 * 1024;
    this.getCurrentUser();
    this.uploader = new FileUploader({
      isHTML5: true,
      maxFileSize: maxFileSize5MB,
      autoUpload: false,
      allowedMimeType: ['image/png', 'image/jpeg', 'image/jpg'],
      headers: [{ name: 'Content-Type', value: 'application/octed-stream' }],
    });

    this.uploader.onWhenAddingFileFailed = (item, filter) => {
      let message = '';
      switch (filter.name) {
        case 'fileSize':
          message =
            'The file"' +
            item.name +
            '" is ' +
            this.formatBytes(item.size) +
            ', this exceeds the maximum allowed size of ' +
            this.formatBytes(maxFileSize5MB);
          break;
        default:
          message = 'Error trying to upload file ' + item.name;
          break;
      }
    };

    this.uploader.onAfterAddingFile = (fileItem: any) => {
      let file = fileItem._file;
      let profilePicture = new FormData();
      profilePicture.append('profilePicture', file);
      this.setProfilePicture(profilePicture);
    };
  }

  private formatBytes(bytes: any, decimals?: any) {
    if (bytes == 0) return '0 Bytes';
    const k = 1024,
      dm = decimals || 2,
      sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
      i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
  }

  changeEmail() {
    this.changeEmailDialogService
      .confirm(this.UserProfileDetailResponse)
      .then(async (apiResponse) => {
        if (apiResponse) {
          this.notificationService.open(apiResponse.message);
        }
      });
    this.getCurrentUser();
  }

  changePassword() {
    this.changePasswordDialogService
      .confirm(this.UserProfileDetailResponse.id)
      .then(async (apiResponse) => {
        if (apiResponse) {
          this.notificationService.open(apiResponse.message);
        }
      });
    this.getCurrentUser();
  }

  updateName() {
    this.formNameDialogService
      .confirmed(this.UserProfileDetailResponse.name)
      .then(async (result) => {
        if (result) {
          this.userService
            .updateName(this.UserProfileDetailResponse.id, result)
            .subscribe((apiResponse) => {
              this.getCurrentUser();
              this.notificationService.open(apiResponse.message);
            });
        }
      });
  }

  setProfilePicture(profilePicture: any) {
    this.userService
      .setProfilePicture(this.UserProfileDetailResponse.id, profilePicture)
      .subscribe((apiResponse) => {
        this.getCurrentUser();
        this.notificationService.open(apiResponse.message);
      });
  }

  getCurrentUser() {
    this.userService.currentUser().subscribe((user) => {
      this.userService
        .findUserProfileByUsername(user.username)
        .subscribe((userProfileDetail) => {
          this.UserProfileDetailResponse = userProfileDetail;
        });
    });
  }

  deleteMyAccount(id: string) {
    const message = `Your account will be deleted forever, are you sure?`;
    const dialogData = new ConfirmDialogRequest('Confirm Action', message);
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      maxWidth: '400px',
      data: dialogData,
    });
    dialogRef.afterClosed().subscribe((result: boolean) => {
      if (result) {
        this.userService.deleteAccount(id).subscribe(
          (apiResponse) => {
            this.notificationService.open(apiResponse.message);
            this.router.navigate(['auth/signin']);
          },
          (error) => {
            this.notificationService.open(error, 'failed');
          }
        );
      }
    });
  }
}
