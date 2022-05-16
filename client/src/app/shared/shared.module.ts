import { CommonModule } from "@angular/common";
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from "@angular/core";
import { FlexLayoutModule } from "@angular/flex-layout";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatTableModule } from "@angular/material/table";
import { RouterModule } from "@angular/router";
import { FileUploadModule } from "ng2-file-upload";
import { NgxSpinnerModule } from "ngx-spinner";
import { NotificationService } from "../core/services/notification.service";
import { ConfirmDialogComponent } from "./component/confirm-dialog/confirm-dialog.component";
import { FormNameDialogComponent } from "./component/form-name-dialog/form-name-dialog.component";
import { FormNameDialogService } from "./component/form-name-dialog/form-name-dialog.service";
import { InputTextFilterModule } from "./directive/input-text-filter/input-text-filter.module";
import { MaterialModule } from "./modules/material/material.module";

@NgModule({
  declarations: [FormNameDialogComponent, ConfirmDialogComponent],
  imports: [
    CommonModule,
    MaterialModule,
    ReactiveFormsModule,
    FormsModule,
    RouterModule,
    FlexLayoutModule,
    InputTextFilterModule,
    NgxSpinnerModule,
    MatTableModule,
  ],
  exports: [
    CommonModule,
    MaterialModule,
    ReactiveFormsModule,
    FormsModule,
    RouterModule,
    FlexLayoutModule,
    FormNameDialogComponent,
    FileUploadModule,
    InputTextFilterModule,
    NgxSpinnerModule,
  ],
  entryComponents: [FormNameDialogComponent, ConfirmDialogComponent],
  providers: [FormNameDialogService, NotificationService],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class SharedModule {}
