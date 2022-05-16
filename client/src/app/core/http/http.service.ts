import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { throwError, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  constructor(
    private http: HttpClient
  ) { }

  findById(url: string, body: Object = {}): Observable<any> {
    return this.http.get(url, body)
      .pipe(catchError(this.handlerError));
  }

  put(url: string, body: Object = {}): Observable<any> {
    return this.http.put(url, body)
      .pipe(catchError(this.handlerError));
  }
  post(url: string, body: Object = {}): Observable<any> {
    return this.http.post(url, body)
      .pipe(catchError(this.handlerError));
  }
  deleteById(url: string): Observable<any> {
    return this.http.delete(url)
      .pipe(catchError(this.handlerError));
  }

  handlerError(error: HttpErrorResponse) {
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Error: ${error.error.message}`;
    } else {
      errorMessage =`Error code: ${error.status}
      Message: ${error.error.message}`;
    }
    console.log(errorMessage);
    return throwError(errorMessage);
  }
}
