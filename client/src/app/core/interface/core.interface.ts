import { Observable } from 'rxjs';

export interface BaseResponse extends DateAuditResponse {
    id: string;
    name: string;
}

export interface DateAuditResponse {
    createdBy: string;
    createdDate: Date;
    updatedBy: string;
    updatedDate: Date;
}

export interface ApiResponse {
    success: string;
    message: string;
}

export class ConfirmDialogRequest {
    constructor(public title: string, public message: string) { }
}

export interface CrudOperations {
    saveName(id: string, body: any): Observable<any>;
    updateName(id: string, body: any): Observable<any>;
    findById(id: string): Observable<any>;
    deleteById(id: string): Observable<any>;
}