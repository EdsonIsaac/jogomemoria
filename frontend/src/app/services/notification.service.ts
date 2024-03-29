import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

import { NotificationComponent } from '../components/notification/notification.component';
import { NotificationType } from '../enums/notification-type.enum';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  constructor(private snackBar: MatSnackBar) {}

  show(message: string, type: NotificationType) {
    this.snackBar.openFromComponent(NotificationComponent, {
      data: {
        message: message,
      },
      duration: 5000,
      horizontalPosition: 'center',
      verticalPosition: 'bottom',
      panelClass: type,
    });
  }
}