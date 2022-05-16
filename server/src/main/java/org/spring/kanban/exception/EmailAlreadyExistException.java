package org.spring.kanban.exception;

public class EmailAlreadyExistException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmailAlreadyExistException(String exception) {
		super(exception);
	}

}