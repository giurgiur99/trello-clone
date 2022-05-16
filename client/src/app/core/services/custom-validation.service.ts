import { Injectable } from '@angular/core';
import { ValidatorFn, AbstractControl, FormGroup } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class CustomValidationService {

  constructor() { }

  PatternAndMinLengthValidator(minLength:number):ValidatorFn {
    return (control:AbstractControl) => {
      if(!control.value){
        return null;
      }
      const regex = new RegExp('^[a-zA-Z0-9_-]*$');
      const valid = regex.test(control.value);
      if(control.value.length >= minLength && !valid){
       return {invalidPassword:true};
      }
    }
  }

  MinLengthAndMaxlengthValidator(minLength:number, maxLength:number){
    return (control:AbstractControl) => {
      if(!control.value){
        return null;
      }

      if(control.value.length < minLength || control.value.length > maxLength){
        return {MinLengthAndMaxlengthValidator:true}
      }
    }
  }
}
