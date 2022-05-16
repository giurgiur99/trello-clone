import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";

import { AdminRoutingModule } from "./admin-routing.module";
import { UsersViewComponent } from "./users-view/users-view.component";
import { SharedModule } from "../shared/shared.module";
import { MainComponent } from "./nav-menu/main.component";
import { UpdateBoardComponent } from "./update-board/update-board.component";
import { DragDropModule } from "@angular/cdk/drag-drop";
import { CheckDetailsComponent } from './check-details/check-details.component';

@NgModule({
  declarations: [UsersViewComponent, MainComponent, UpdateBoardComponent, CheckDetailsComponent],
  imports: [CommonModule, AdminRoutingModule, SharedModule, DragDropModule],
})
export class AdminModule {}
