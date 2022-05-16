package org.spring.kanban.exception;

import org.springframework.http.HttpStatus;

public class ErrorResponse extends ApiError {

	private String detail;

	public ErrorResponse(HttpStatus httpStatus, String message, String detail) {
		super(httpStatus, message);
		this.detail = detail;
	}

	public String getDetail() {
		return detail;
	}
}