import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpService } from 'src/app/core/http/http.service';
import { environment } from 'src/environments/environment';
import { ApiResponse } from '../../../core/interface/core.interface';
import { NameRequest } from '../../user/interface/user.interface';
import { PositionService } from './position.service';

@Injectable({
  providedIn: 'root'
})
export class ColumnService {

  constructor(
    private httpService: HttpService,
    private positionService: PositionService
  ) {
  }

  saveName(idBoard: string, nameRequest: NameRequest): Observable<ApiResponse> {
    return this.httpService.post(`${environment.columnEndpoint}/${idBoard}`, nameRequest);
  }

  updateName(idColumn: string, nameRequest: NameRequest): Observable<ApiResponse> {
    return this.httpService.put(`${environment.columnEndpoint}/${idColumn}`, nameRequest);
  }

  findById(id: string): Observable<any> {
    return this.httpService.findById(`${environment.columnEndpoint}/${id}`);
  }

  deleteById(id: string): Observable<ApiResponse> {
    return this.httpService.deleteById(`${environment.columnEndpoint}/${id}`);
  }

  changePosition(idReference: string, currentPosition: number, desiredPosition: number): Observable<ApiResponse> {
    return this.positionService.changePosition(`${environment.columnEndpoint}/${idReference}/${currentPosition}/${desiredPosition}`);
  }

}
