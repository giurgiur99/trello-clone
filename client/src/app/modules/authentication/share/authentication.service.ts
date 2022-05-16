import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { HttpService } from 'src/app/core/http/http.service';
import { ApiResponse } from 'src/app/core/interface/core.interface';
import { environment } from 'src/environments/environment';
import { JwtResponse, SignInPayload, SignUpPayload } from '../interface/authentication.interface';


@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private router: Router,
    private httpService: HttpService) {
  }

  public setToken(token?: string) {
    return localStorage.setItem('token', token);
  }

  public getToken(): string {
    return localStorage.getItem('token');
  }

  public signIn(data: SignInPayload): Observable<JwtResponse> {
    return this.httpService.post(`${environment.authenticationEndpoint}/signin`, data)
      .pipe(
        map((response: JwtResponse) => {
          this.setToken(response.accessToken);         
          return response;
        })
      );
  }

  public signUp(data: SignUpPayload): Observable<ApiResponse> {
    return this.httpService.post(`${environment.authenticationEndpoint}/signup`, data);
  }

  public logout(){
    localStorage.removeItem('token');
  }
}
