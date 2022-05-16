import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FileUploader } from 'ng2-file-upload';
import { NotificationService } from 'src/app/core/services/notification.service';
import {
  CardDetailsResponse,
  DescriptionRequest,
} from '../../interface/board-details.interface';
import { CardService } from '../../share/card.service';

@Component({
  selector: 'app-card-details',
  templateUrl: './card-details.component.html',
  styleUrls: ['./card-details.component.scss'],
})
export class CardDetailsComponent implements OnInit {
  card: CardDetailsResponse = {
    description: 'Loading...',
    idColumn: '',
    attachments: [],
    id: '',
    name: 'Card',
    createdBy: '',
    createdDate: undefined,
    updatedBy: '',
    updatedDate: undefined,
  };
  cardUpdate: DescriptionRequest = {} as DescriptionRequest;
  attachmentExtensions: string[] = [];
  isImage: boolean = false;
  IsShown: boolean = false;
  uploader: FileUploader;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private cardService: CardService,
    private notificationService: NotificationService
  ) {}

  ngOnInit() {
    this.findCardById();
    let maxFileSize5MB = 5 * 1024 * 1024;

    this.uploader = new FileUploader({
      isHTML5: true,
      maxFileSize: maxFileSize5MB,
      autoUpload: false,
      headers: [{ name: 'Content-Type', value: 'application/octed-stream' }],
    });
    this.uploader.onWhenAddingFileFailed = (item, filter) => {
      let message = '';
      switch (filter.name) {
        case 'fileSize':
          message =
            'The file"' +
            item.name +
            '" is ' +
            this.formatBytes(item.size) +
            ', this exceeds the maximum allowed size of ' +
            this.formatBytes(maxFileSize5MB);
          break;
        default:
          message = 'Error trying to upload file ' + item.name;
          break;
      }
      const durationIn5Seconds = 5 * 1000;
      this.notificationService.open(message, 'Warning', durationIn5Seconds);
    };

    this.uploader.onAfterAddingAll = (files: any) => {
      this.uploadAttachments(files);
    };

    this.uploader.onCompleteAll = (): any => {
      console.log('complete');
    };
  }

  async uploadAttachments(filesList: any) {
    let files = filesList;
    let formData = new FormData();
    files.forEach((file: any) => {
      formData.append('file[]', file._file);
    });
    await this.cardService.uploadFiles(this.data.id, formData).subscribe(
      (apiResponse) => {
        this.notificationService.open(apiResponse.message);
        this.findCardById();
      },
      (error) => {
        this.notificationService.open(error, 'failed');
      }
    );
  }

  private formatBytes(bytes: any, decimals?: any) {
    if (bytes == 0) return '0 Bytes';
    const k = 1024,
      dm = decimals || 2,
      sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
      i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
  }

  deleteAttachmentById(idAttachment: string) {
    this.cardService.deleteAttachmentById(this.data.id, idAttachment).subscribe(
      (apiResponse) => {
        this.notificationService.open(apiResponse.message);
        this.findCardById();
      },
      (error) => {
        this.notificationService.open(error, 'failed');
      }
    );
  }

  findCardById() {
    this.cardService.findById(this.data.id).subscribe((card) => {
      this.card = card;
      const attachments = card.attachments;
      attachments.forEach((attachment) => {
        let extension = attachment.name.split('.').pop();
        this.attachmentExtensions.push(extension);
      });
    });
  }

  updateCardDescription() {
    this.cardService.updateDescription(this.data.id, this.cardUpdate).subscribe(
      (apiResponse) => {
        this.notificationService.open(apiResponse.message);
        this.IsShown = false;
        this.findCardById();
      },
      (error) => {
        this.notificationService.open(error, 'failed');
      }
    );
  }

  setCover(cardId: string, idAttachment: string) {
    this.cardService.setCover(cardId, idAttachment).subscribe(
      (apiResponse) => {
        this.notificationService.open(apiResponse.message);
        this.findCardById();
      },
      (error) => {
        this.notificationService.open(error, 'failed');
      }
    );
  }

  removeCover(cardId: string, idAttachment: string) {
    this.cardService.removeCover(cardId, idAttachment).subscribe(
      (apiResponse) => {
        this.notificationService.open(apiResponse.message);
        this.findCardById();
      },
      (error) => {
        this.notificationService.open(error, 'failed');
      }
    );
  }

  closeCardForm(cardDescription: string) {
    this.cardUpdate.description = cardDescription;
    this.IsShown = false;
  }

  toggleTextareaForm() {
    this.IsShown = !this.IsShown;
    this.IsShown = false;
  }
}
