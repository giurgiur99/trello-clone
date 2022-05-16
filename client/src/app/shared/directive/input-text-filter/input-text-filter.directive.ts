import { Directive, Output, EventEmitter } from '@angular/core';


@Directive({
  selector: '[InputTextFilter]',
  host: {
    "(input)": 'onInputChange($event)'
  }
})
export class InputTextFilterDirective {

  @Output() ngModelChange: EventEmitter<any> = new EventEmitter();
  value: any;

  constructor() {
  }

  onInputChange($event: any) {
    this.value = $event.target.value.replace(/\s+/g, ' ').trim();
    this.ngModelChange.emit(this.value);
  }

}
