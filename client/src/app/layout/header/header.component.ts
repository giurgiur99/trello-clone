import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { UserService } from "src/app/modules/user/shared/user.service";
import { AuthenticationService } from "src/app/modules/authentication/share/authentication.service";

@Component({
  selector: "app-header",
  templateUrl: "./header.component.html",
  styleUrls: ["./header.component.scss"],
})
export class HeaderComponent implements OnInit {
  constructor(
    private router: Router,
    private userService: UserService,
    private authenticationService: AuthenticationService
  ) {}

  ngOnInit(): void {}

  routerBoards() {
    this.userService.currentUser().subscribe((user) => {
      this.router.navigate([`${user.username}/boards`]);
    });
  }

  routerProfile() {
    this.userService.currentUser().subscribe((user) => {
      this.router.navigate([`user/${user.username}/profile`]);
    });
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(["auth/signin"]);
  }
}
