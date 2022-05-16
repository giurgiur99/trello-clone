import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpService } from 'src/app/core/http/http.service';
import { ApiResponse } from 'src/app/core/interface/core.interface';
import { BoardsResponse, EmailRequest, NameRequest, PasswordRequest, UserProfileDetailResponse, userSummary } from 'src/app/modules/user/interface/user.interface';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpService: HttpService) { }

  currentUser(): Observable<userSummary> {
    return this.httpService.findById(`${environment.userEndpoint}/me`);
  }

  findUserProfileByUsername(username: string): Observable<UserProfileDetailResponse> {
    return this.httpService.findById(`${environment.userEndpoint}/${username}`);
  }

  findBoardsByUsername(username: string): Observable<BoardsResponse[]> {
    return this.httpService.findById(`${environment.userEndpoint}/${username}/boards`);
  }

  changeEmailById(id: string, emailRequest: EmailRequest): Observable<ApiResponse> {
    return this.httpService.put(`${environment.userEndpoint}/${id}/changeEmail`, emailRequest);
  }

  changePasswordById(id: string, passwordRequest: PasswordRequest): Observable<any> {
    return this.httpService.put(`${environment.userEndpoint}/${id}/changePassword`, passwordRequest);
  }

  setProfilePicture(id: string, profilePicture: any): Observable<ApiResponse> {
    return this.httpService.put(`${environment.userEndpoint}/${id}/setProfilePicture`, profilePicture);
  }

  updateName(id: string, nameRequest: NameRequest): Observable<ApiResponse> {
    return this.httpService.put(`${environment.userEndpoint}/${id}`, nameRequest);
  }

  deleteAccount(id:string):Observable<ApiResponse>{
    return this.httpService.deleteById(`${environment.userEndpoint}/${id}`);
  }

}
