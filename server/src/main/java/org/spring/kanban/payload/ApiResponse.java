package org.spring.kanban.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public abstract class ApiResponse {
	private Boolean success;
	private String message;

	public ApiResponse(Boolean success) {
		this.success = success;
	}

	public void addMessage(String message) {
		this.setMessage(message);
	}

}
