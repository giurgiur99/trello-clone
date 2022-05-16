import { Component, OnInit, ViewChild } from "@angular/core";
import { MatPaginator } from "@angular/material/paginator";
import { MatTableDataSource } from "@angular/material/table";
import { UserService } from "../users-view/services/user.service";

@Component({
  selector: "app-check-details",
  templateUrl: "./check-details.component.html",
  styleUrls: ["./check-details.component.scss"],
})
export class CheckDetailsComponent implements OnInit {
  displayedColumns: string[] = ["name", "username", "email"];
  data: User[] = [];
  dataSource = new MatTableDataSource<User>(this.data);

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private userService: UserService) {}

  async refreshTab() {
    this.dataSource.paginator = this.paginator;
    this.data = await this.getCustomers();
    this.dataSource = new MatTableDataSource(this.data);
    console.log(this.data);
  }

  ngOnInit(): void {
    this.refreshTab();
  }

  async getCustomers() {
    return await this.userService.getCustomer();
  }

  async onDeleteUserClick(user: User) {
    await this.userService.deleteUser(user);
    this.refreshTab();
  }
}

export interface User {
  name: string;
  username: string;
  email: string;
}
