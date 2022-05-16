import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BoardDetailsComponent } from './board-details.component';


const routes: Routes = [
  {
    path:":id",component:BoardDetailsComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BoardDetailsRoutingModule { }
