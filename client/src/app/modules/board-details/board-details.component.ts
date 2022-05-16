import { CdkDragDrop } from '@angular/cdk/drag-drop';
import { Component, Inject, Injectable, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';

import { ConfirmDialogRequest } from 'src/app/core/interface/core.interface';
import { NotificationService } from 'src/app/core/services/notification.service';
import { ConfirmDialogComponent } from 'src/app/shared/component/confirm-dialog/confirm-dialog.component';
import { FormNameDialogService } from 'src/app/shared/component/form-name-dialog/form-name-dialog.service';
import { BoardDetailsResponse } from './interface/board-details.interface';
import { CardDetailsComponent } from './pages/card-details/card-details.component';
import { BoardDetailsService } from './share/boards-details.service';
import { CardService } from './share/card.service';
import { ColumnService } from './share/column.service';

@Component({
  selector: 'app-board-details',
  templateUrl: './board-details.component.html',
  styleUrls: ['./board-details.component.scss'],
})
export class BoardDetailsComponent implements OnInit {
  board: BoardDetailsResponse = {
    columns: [],
    id: '',
    name: '',
    createdBy: '',
    createdDate: undefined,
    updatedBy: '',
    updatedDate: undefined,
  };
  id: string = '';
  connectedTo: string[] = [];

  constructor(
    private boardService: BoardDetailsService,
    private columnService: ColumnService,
    private cardService: CardService,
    private formNameDialogService: FormNameDialogService,
    private activatedRoute: ActivatedRoute,
    public dialog: MatDialog,
    private spinner: NgxSpinnerService,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.spinner.show();
    this.activatedRoute.params.subscribe((params) => {
      this.id = params['id'];
    });
    this.findBoardsById();
    this.spinner.hide();
  }

  public openCardDialog(id: string, column: string): void {
    let dialogRef = this.dialog.open(CardDetailsComponent, {
      autoFocus: false,
      maxHeight: '90vh',
      data: { id: id, column: column },
    });
    dialogRef.afterClosed().subscribe((result) => {
      this.findBoardsById();
    });
  }

  findBoardsById() {
    this.boardService.findById(this.id).subscribe((board) => {
      this.board = board;
      const columns = board.columns;
      this.connectedTo = [];
      columns.forEach((column) => {
        this.connectedTo.push(column.id);
      });
    });
  }

  saveColumn() {
    this.formNameDialogService.confirmed().then(async (result) => {
      if (result) {
        this.columnService.saveName(this.id, result).subscribe(
          (column) => {
            this.notificationService.open(column.message);
            this.findBoardsById();
          },
          (error) => {
            this.notificationService.open(error, 'failed');
          }
        );
      }
    });
  }

  updateColumnName(idColumn: string, name: string) {
    this.formNameDialogService.confirmed(name).then(async (result) => {
      if (result) {
        this.columnService.updateName(idColumn, result).subscribe(
          (column) => {
            this.notificationService.open(column.message);
            this.findBoardsById();
          },
          (error) => {
            this.notificationService.open(error, 'failed');
          }
        );
      }
    });
  }

  deleteColumn(id: string) {
    const message = `Are you sure you want to do this?
    If you have Cards and Attachments, they will be permanently deleted.`;
    const dialogData = new ConfirmDialogRequest('Confirm Action', message);

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      maxWidth: '400px',
      data: dialogData,
    });
    dialogRef.afterClosed().subscribe((result: boolean) => {
      if (result) {
        this.columnService.deleteById(id).subscribe(
          (column) => {
            this.notificationService.open(column.message);
            this.findBoardsById();
          },
          (error) => {
            this.notificationService.open(error, 'failed');
          }
        );
      }
    });
  }

  saveCard(idColumn: string) {
    this.formNameDialogService.confirmed().then(async (result) => {
      if (result) {
        this.cardService.saveName(idColumn, result).subscribe(
          (apiResponse) => {
            this.notificationService.open(apiResponse.message);
            this.findBoardsById();
          },
          (error) => {
            this.notificationService.open(error, 'failed');
          }
        );
      }
    });
  }

  deleteCard(id: string) {
    this.cardService.deleteById(id).subscribe(
      (apiResponse) => {
        this.notificationService.open(apiResponse.message);
        this.findBoardsById();
      },
      (error) => {
        this.notificationService.open(error, 'failed');
      }
    );
  }

  updateCardName(id: string, name: string) {
    this.formNameDialogService.confirmed(name).then(async (result) => {
      if (result) {
        this.cardService.updateName(id, result).subscribe(
          (apiResponse) => {
            this.notificationService.open(apiResponse.message);
            this.findBoardsById();
          },
          (error) => {
            this.notificationService.open(error, 'failed');
          }
        );
      }
    });
  }

  onDropColumns(event: CdkDragDrop<any>) {
    const idBoardReference = event.previousContainer.data[0]?.idBoard;
    const currentIndex = event.previousIndex;
    const desiredIndex = event.currentIndex;
    if (currentIndex !== desiredIndex) {
      this.columnService
        .changePosition(idBoardReference, currentIndex, desiredIndex)
        .subscribe(
          (apiResponse) => {
            this.notificationService.open(apiResponse.message);
            this.findBoardsById();
          },
          (error) => {
            this.notificationService.open(error, 'failed');
          }
        );
    } else {
      this.notificationService.open('Failed to change position');
    }
  }

  onDropCards(event: CdkDragDrop<any>) {
    const idColumn = event.previousContainer.data[0]?.idColumn;
    const currentIndex = event.previousIndex;
    const desiredIndex = event.currentIndex;
    const desiredData = event.container.data[desiredIndex];
    const currentData = event.previousContainer.data[currentIndex];

    if (
      desiredData === undefined ||
      currentData.idColumn !== desiredData.idColumn
    ) {
      this.cardService
        .moveCardToAnotherColumn(
          currentData.id,
          desiredIndex,
          event.container.id
        )
        .subscribe(
          (apiResponse) => {
            this.notificationService.open(apiResponse.message);
            this.findBoardsById();
          },
          (error) => {
            this.notificationService.open(error, 'failed');
          }
        );
    } else if (
      currentData.idColumn === desiredData.idColumn &&
      currentData.position !== desiredData.position
    ) {
      this.cardService
        .changeCardPosition(idColumn, currentIndex, desiredIndex)
        .subscribe(
          (apiResponse) => {
            this.notificationService.open(apiResponse.message);
            this.findBoardsById();
          },
          (error) => {
            this.notificationService.open(error, 'failed');
          }
        );
    } else {
      this.notificationService.open('Failed to change position');
    }
  }
}
