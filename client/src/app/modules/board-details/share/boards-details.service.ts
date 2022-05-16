import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { HttpService } from "src/app/core/http/http.service";
import { ApiResponse } from "src/app/core/interface/core.interface";
import { environment } from "src/environments/environment";
import { NameRequest } from "../../user/interface/user.interface";
import { BoardDetailsResponse } from "../interface/board-details.interface";

@Injectable({
  providedIn: "root",
})
export class BoardDetailsService {
  constructor(private httpService: HttpService) {}

  saveName(nameRequest: NameRequest): Observable<ApiResponse> {
    return this.httpService.post(`${environment.boardEndpoint}`, nameRequest);
  }

  updateName(id: string, nameRequest: NameRequest): Observable<ApiResponse> {
    return this.httpService.put(
      `${environment.boardEndpoint}/${id}`,
      nameRequest
    );
  }

  findById(id: string): Observable<BoardDetailsResponse> {
    return this.httpService.findById(`${environment.boardEndpoint}/${id}`);
  }

  deleteById(id: string): Observable<ApiResponse> {
    return this.httpService.deleteById(`${environment.boardEndpoint}/${id}`);
  }
}
