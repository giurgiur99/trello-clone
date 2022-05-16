package org.spring.kanban.exception;

import java.util.List;

import org.springframework.http.HttpStatus;


public class ValidationErrorResponse extends ApiError {

	private List<String> details;

	public ValidationErrorResponse(HttpStatus httpStatus, String message, List<String> details) {
		super(httpStatus, message);
		this.details = details;
	}

	public List<String> getDetails() {
		return details;
	}
}
