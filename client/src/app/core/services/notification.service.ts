import { Injectable, NgZone } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  durationIn2000Milliseconds: number = 2000;
  durationIn5000Milliseconds: number = 5000;

  constructor(private snackBar: MatSnackBar, private zone: NgZone) { }

  open(message: string, action = 'success', duration = this.durationIn2000Milliseconds) {
    this.zone.run(() => {
      if (action.valueOf() == 'failed') {
        this.snackBar.open(message, action, { duration: this.durationIn5000Milliseconds });
      } else {
        this.snackBar.open(message, action, { duration });
      }
    });
  }
}
