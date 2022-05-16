import { Injectable } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ChangeEmailDialogComponent } from '../components/change-email-dialog/change-email-dialog.component';
import { UserProfileDetailResponse } from '../interface/user.interface';

@Injectable({
  providedIn: 'root'
})
export class ChangeEmailDialogService {

  constructor(
    private dialog:MatDialog
  ) { }

  async confirm(userProfileDetail:UserProfileDetailResponse):Promise<any>{
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = false;
    dialogConfig.maxWidth = "300px";
    dialogConfig.data = userProfileDetail;
    
    const dialogRef = this.dialog.open(ChangeEmailDialogComponent,dialogConfig);
    return await dialogRef.afterClosed()
      .toPromise().then(result => {
        return Promise.resolve(result);
      });
  }
}
