import { Injectable } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ChangePasswordDialogComponent } from '../components/change-password-dialog/change-password-dialog.component';

@Injectable({
  providedIn: 'root'
})
export class ChangePasswordDialogService {

  constructor(
    private dialog: MatDialog
  ) { }

  async confirm(IdUser: string): Promise<any> {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = false;
    dialogConfig.maxWidth = "300px";
    dialogConfig.data = {
      id: IdUser
    };
    const dialogRef = this.dialog.open(ChangePasswordDialogComponent, dialogConfig);
    return await dialogRef.afterClosed()
      .toPromise().then(result => {
        return Promise.resolve(result);
      });
  }

}
