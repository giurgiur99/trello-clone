import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { ConfirmDialogRequest } from 'src/app/core/interface/core.interface';
import { NotificationService } from 'src/app/core/services/notification.service';
import { BoardDetailsService } from 'src/app/modules/board-details/share/boards-details.service';
import { UserService } from 'src/app/modules/user/shared/user.service';
import { ConfirmDialogComponent } from 'src/app/shared/component/confirm-dialog/confirm-dialog.component';
import { FormNameDialogService } from 'src/app/shared/component/form-name-dialog/form-name-dialog.service';
import { BoardsResponse } from '../../interface/user.interface';


@Component({
  selector: 'app-boards',
  templateUrl: './boards.component.html',
  styleUrls: ['./boards.component.scss']
})
export class BoardsComponent implements OnInit {

  username: string = '';
  id: string = '';
  boards: BoardsResponse[] = [];


  constructor(
    private userService: UserService,
    private activatedRoute: ActivatedRoute,
    private boardService: BoardDetailsService,
    private formNameDialogService: FormNameDialogService,
    private notificationService: NotificationService,
    private dialog: MatDialog) { }

  ngOnInit(): void {
    this.activatedRoute.parent.params.subscribe(
      params => {
        this.username = params['username'];
      }
    )
    this.currentUser();
    this.findBoardsByUsername();
  }

  findBoardsByUsername() {
    this.userService.findBoardsByUsername(this.username)
      .subscribe(boards => {
        this.boards = boards;
      });
  }

  currentUser() {
    this.userService.currentUser().subscribe(user => {
      this.id = user.id;
    })
  }

  saveBoard() {
    this.formNameDialogService.confirmed().then(
      async name => {
        if (name) {
          this.boardService.saveName(name).subscribe(apiResponse => {
            this.findBoardsByUsername();
            this.notificationService.open(apiResponse.message);
          }, error => {
            this.notificationService.open(error, 'failed');
          });
        }
      }
    )
  }

  deleteBoard(id: string) {
    const message = `Are you sure you want to do this?
    If you have Cards and Attachments, they will be permanently deleted.`;
    const dialogData = new ConfirmDialogRequest("Confirm Action", message);

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      maxWidth: "400px",
      data: dialogData
    });

    dialogRef.afterClosed().subscribe((data: any) => {
      if (data) {
        this.boardService.deleteById(id).subscribe(apiResponse => {
          this.findBoardsByUsername();
          this.notificationService.open(apiResponse.message);
        }, error => {
          this.notificationService.open(error, 'failed');
        });
      }
    })
  }

  updateBoard(id: string, name: string) {
    this.formNameDialogService.confirmed(name).then(
      async board => {
        if (board) {
          this.boardService.updateName(id, board).subscribe(apiResponse => {
            this.findBoardsByUsername();
            this.notificationService.open(apiResponse.message);
          }, error => {
            this.notificationService.open(error, 'failed');
          });
        }
      }
    )
  }
}
