import { BaseResponse } from 'src/app/core/interface/core.interface';

export interface UserProfileDetailResponse {
  id: string;
  name: string;
  username: string;
  email: string;
  profilePicture?: File;
  joinedAt: Date;
}

export interface PasswordRequest {
  oldPassword: string;
  newPassword: string;
  confirmPassword: string;
}

export interface EmailRequest {
  currentPassword: string;
  email: string;
}

export interface NameRequest {
  name: string;
}

export interface BoardsResponse extends BaseResponse {}

export interface userSummary {
  id: string;
  username: string;
  name: string;
}
