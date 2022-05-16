package org.spring.kanban.exception;

public class EmailInvalidException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmailInvalidException(String exception) {
		super(exception);
	}

}