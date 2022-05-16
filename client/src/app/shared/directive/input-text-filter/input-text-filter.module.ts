import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InputTextFilterDirective } from './input-text-filter.directive';

@NgModule({
  declarations: [InputTextFilterDirective],
  imports: [
    CommonModule
  ], exports: [
    InputTextFilterDirective
  ]
})
export class InputTextFilterModule { }
