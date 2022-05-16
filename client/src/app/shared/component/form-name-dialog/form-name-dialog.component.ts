import { Component, OnInit, Inject, HostListener } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-form-name',
  templateUrl: './form-name-dialog.component.html',
  styleUrls: ['./form-name-dialog.component.scss']
})
export class FormNameDialogComponent implements OnInit {

  constructor(
    private dialogRef: MatDialogRef<FormNameDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {
      name: string
    }
  ) { }

  ngOnInit(): void {
  }

  public cancel() {
    this.close(false);
  }

  public close(value: any): void {
    this.dialogRef.close(value);
  }

  public confirm() {
    this.close(this.data);
  }

  @HostListener("window:keydown.esc")
  public onEsc() {
    this.close(false);
  }

}
