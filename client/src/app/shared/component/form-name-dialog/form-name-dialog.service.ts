import { Injectable } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { FormNameDialogComponent } from './form-name-dialog.component';

@Injectable({
  providedIn: 'root'
})
export class FormNameDialogService {

  constructor(
    private dialog: MatDialog    
  ) { }

  async confirmed(name?: string): Promise<any> {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.data = {
      name: name
    };
    const dialogRef = this.dialog.open(FormNameDialogComponent, dialogConfig);
    return await dialogRef.afterClosed()
      .toPromise().then(result => {
        return Promise.resolve(result);
      })
  }

}
