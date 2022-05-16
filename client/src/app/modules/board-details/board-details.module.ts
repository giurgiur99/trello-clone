import { DragDropModule } from '@angular/cdk/drag-drop';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { BoardDetailsRoutingModule } from './board-details-routing.module';
import { BoardDetailsComponent } from './board-details.component';
import { CardDetailsComponent } from './pages/card-details/card-details.component';



@NgModule({
  declarations: [
    BoardDetailsComponent,
    CardDetailsComponent],
  imports: [
    CommonModule, 
    BoardDetailsRoutingModule,
    SharedModule,
    DragDropModule
  ]
})
export class BoardDetailsModule { }
