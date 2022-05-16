import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpService } from 'src/app/core/http/http.service';
import { ApiResponse, CrudOperations } from 'src/app/core/interface/core.interface';
import { CardDetailsResponse, DescriptionRequest, NameRequest } from 'src/app/modules/board-details/interface/board-details.interface';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CardService implements CrudOperations {

  constructor(private httpService: HttpService) {
  }

  saveName(idColumn: string, nameRequest: NameRequest): Observable<ApiResponse> {
    return this.httpService.post(`${environment.cardEndpoint}/${idColumn}`, nameRequest);
  }

  updateName(idColumn: string, nameRequest: NameRequest): Observable<ApiResponse> {
    return this.httpService.put(`${environment.cardEndpoint}/${idColumn}`, nameRequest);
  }

  findById(id: string): Observable<CardDetailsResponse> {
    return this.httpService.findById(`${environment.cardEndpoint}/${id}`);
  }

  deleteById(id: string): Observable<ApiResponse> {
    return this.httpService.deleteById(`${environment.cardEndpoint}/${id}`);
  }

  uploadFiles(id: string, files: any): Observable<ApiResponse> {
    return this.httpService.put(`${environment.cardEndpoint}/${id}/attachment`, files);
  }

  updateDescription(id: string, descriptionRequest: DescriptionRequest): Observable<ApiResponse> {
    return this.httpService.put(`${environment.cardEndpoint}/${id}/description`, descriptionRequest);
  }

  deleteAttachmentById(id: string, idCard: string): Observable<ApiResponse> {
    return this.httpService.deleteById(`${environment.cardEndpoint}/${id}/attachment/${idCard}`);
  }

  setCover(cardId: string, idAttachment: string): Observable<ApiResponse> {
    return this.httpService.put(`${environment.cardEndpoint}/${cardId}/attachment/${idAttachment}/cover`, '');
  }

  removeCover(cardId: string, idAttachment: string): Observable<ApiResponse> {
    return this.httpService.deleteById(`${environment.cardEndpoint}/${cardId}/attachment/${idAttachment}/cover`);
  }

  moveCardToAnotherColumn(idCurrentCard: string, desiredPosition: number, idColumn: string): Observable<ApiResponse> {
    return this.httpService.put(`${environment.cardEndpoint}/${idColumn}/moveCardToAnotherColumn/${idCurrentCard}/${desiredPosition}`, '');
  }

  changePosition(idReference: string, currentPosition: number, desiredPosition: number): Observable<ApiResponse> {
    return this.httpService.put(`${environment.columnEndpoint}/${idReference}/${currentPosition}/${desiredPosition}`, '');
  }

  changeCardPosition(idReference: string, currentPosition: number, desiredPosition: number): Observable<ApiResponse> {
    return this.httpService.put(`${environment.cardEndpoint}/${idReference}/${currentPosition}/${desiredPosition}`, '');
  }

}
