import { BaseResponse } from 'src/app/core/interface/core.interface';

export interface BoardDetailsResponse extends BaseResponse {
  columns: ColumnDetails[];
}

export interface ColumnDetails extends BaseResponse {
  idBoard: string;
  position: number;
  cards: CardDetails;
}

export interface CardDetails extends BaseResponse {
  idColumn: string;
  idBoard: string;
  position: number;
  cover: CoverDetails;
  fileSize: number;
}

export interface CoverDetails {
  idAttachment: string;
  name: string;
  file: File;
  contentType: string;
  idCard: string;
  idBoard: string;
}

export interface CardDetailsResponse extends BaseResponse {
  description: string;
  idColumn: string;
  cover?: CoverDetails;
  attachments: AttachmentDetails[];
}

export interface AttachmentDetails extends BaseResponse {
  file: File;
  contentType: string;
  idCard: number;
  idBoard: string;
}

export interface NameRequest {
  name: string;
}

export interface DescriptionRequest {
  description: string;
}
