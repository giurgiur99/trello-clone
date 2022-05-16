import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LayoutComponent } from './layout.component';
import { HeaderModule } from './header/header.module';
import { MaterialModule } from '../shared/modules/material/material.module';
import { LayoutRoutingModule } from './layout-routing.module';
import { SharedModule } from '../shared/shared.module';
import { CoreModule } from '@angular/flex-layout';

@NgModule({
  declarations: [LayoutComponent],
  imports: [
    CommonModule,
    HeaderModule,
    LayoutRoutingModule,
    SharedModule,
    CoreModule,
  ],
})
export class LayoutModule {}
