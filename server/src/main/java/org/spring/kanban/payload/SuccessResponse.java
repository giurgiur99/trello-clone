package org.spring.kanban.payload;

public class SuccessResponse extends ApiResponse {

	public SuccessResponse(String message) {
		super(true);
		addMessage(message);
	}

	public SuccessResponse() {
		this(null);
	}

}