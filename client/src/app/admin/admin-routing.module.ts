import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { CheckDetailsComponent } from "./check-details/check-details.component";
import { UpdateBoardComponent } from "./update-board/update-board.component";
import { UsersViewComponent } from "./users-view/users-view.component";

const routes: Routes = [
  {
    path: "",
    component: UsersViewComponent,
  },
  {
    path: "board",
    component: UpdateBoardComponent,
  },
  {
    path: "details",
    component: CheckDetailsComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AdminRoutingModule {}
