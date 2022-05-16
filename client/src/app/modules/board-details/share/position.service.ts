import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpService } from 'src/app/core/http/http.service';

@Injectable({
  providedIn: 'root'
})
export class PositionService {

  constructor(protected httpService: HttpService) {
  }

  changePosition(url: string): Observable<any> {
    return this.httpService.put(`${url}`);
  }
}
