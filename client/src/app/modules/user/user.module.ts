import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { ChangeEmailDialogComponent } from './components/change-email-dialog/change-email-dialog.component';
import { ChangePasswordDialogComponent } from './components/change-password-dialog/change-password-dialog.component';
import { BoardsComponent } from './pages/boards/boards.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { ChangeEmailDialogService } from './shared/change-email-dialog.service';
import { ChangePasswordDialogService } from './shared/change-password-dialog.service';
import { UserRoutingModule } from './user-routing.module';
import { UserComponent } from './user.component';
 
@NgModule({
  declarations: [UserComponent, BoardsComponent, ProfileComponent, ChangePasswordDialogComponent, ChangeEmailDialogComponent],
  imports: [
    CommonModule,
    UserRoutingModule,
    SharedModule
  ],
  providers:[ChangePasswordDialogService,ChangeEmailDialogService]
})
export class UserModule { }
