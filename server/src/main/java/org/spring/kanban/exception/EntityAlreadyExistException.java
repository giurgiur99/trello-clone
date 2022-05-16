package org.spring.kanban.exception;

public class EntityAlreadyExistException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EntityAlreadyExistException(String exception) {
		super(exception);
	}
}