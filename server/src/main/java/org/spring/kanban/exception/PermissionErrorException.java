package org.spring.kanban.exception;

public class PermissionErrorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String entityName;

	public PermissionErrorException(String entityName) {
		super(String.format("You are not allowed to access the %s", entityName));
		this.entityName = entityName;
	}

	public String getEntityName() {
		return entityName;
	}
}
