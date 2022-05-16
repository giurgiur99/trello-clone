import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom } from "rxjs";
import { Boards } from "../../interfaces/interfaces";
import { User } from "../users-view.component";

@Injectable({
  providedIn: "root",
})
export class UserService {
  url: string = "http://localhost:8080/admin/";

  constructor(private http: HttpClient) {}

  async getCustomer() {
    return await firstValueFrom(this.http.get<User[]>(this.url + "users"));
  }

  async deleteUser(user: User) {
    return await firstValueFrom(this.http.delete(this.url + user.username));
  }

  async findBoardsByUsername(username: string) {
    return await firstValueFrom(
      this.http.get<Boards[]>(this.url + username + "/boards")
    );
  }
}
