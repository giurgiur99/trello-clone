import { Component, OnInit, Inject, HostListener } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ConfirmDialogRequest } from 'src/app/core/interface/core.interface';

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.scss']
})
export class ConfirmDialogComponent implements OnInit {
  title: string;
  message: string;

  constructor(private dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) private data: ConfirmDialogRequest) {
    this.title = data.title;
    this.message = data.message;
  }

  ngOnInit(): void {
  }

  @HostListener("keydown.enter")
  confirm(): void {
    this.close(true);
  }

  cancel(): void {
    this.close(false);
  }

  close(value: any): void {
    this.dialogRef.close(value);
  }

  @HostListener("keydown.esc")
  onEsc() {
    this.close(false);
  }
}
