import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './header.component';
import { SharedModule } from 'src/app/shared/shared.module';



@NgModule({
  declarations: [HeaderComponent],
  imports: [
    CommonModule,
    SharedModule 
  ],exports :[
    HeaderComponent 
  ]
})
export class HeaderModule { }
