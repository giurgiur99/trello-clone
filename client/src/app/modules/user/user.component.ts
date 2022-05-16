import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";

@Component({
  selector: "app-user",
  templateUrl: "./user.component.html",
  styleUrls: ["./user.component.scss"],
})
export class UserComponent implements OnInit {
  username: string = "";
  navLinks: any[] = [];

  constructor(private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.getUsernameFromRoute();
    this.getNavLinks();
  }

  getNavLinks() {
    this.navLinks = [
      {
        label: "Boards",
        link: "boards",
        index: 0,
      },
      {
        label: "Profile",
        link: "profile",
        index: 1,
      },
    ];
  }

  getUsernameFromRoute() {
    this.activatedRoute.params.subscribe((params) => {
      console.log(params["username"]);
      console.log(params);
      this.username = params["username"];
    });
  }
}
