package org.spring.kanban.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

	/*
	 * Error handle for invalid parameter for @ID
	 * 
	 */
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		log.error("Failed to convert parameter {} ", ex.getMessage());
		return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST,
				"Failed to convert parameter {id=" + ex.getValue() + "} to ObjectId", ex.getLocalizedMessage()));
	}

	/*
	 * Error handle for @Valid
	 * 
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> details = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + " " + error.getDefaultMessage()).collect(Collectors.toList());
		return buildResponseEntity(new ValidationErrorResponse(HttpStatus.BAD_REQUEST, "Validation Failed", details));
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		return buildResponseEntity(new ErrorResponse(HttpStatus.NOT_FOUND, "Not found", ex.getLocalizedMessage()));
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return buildResponseEntity(new ErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getLocalizedMessage()));
	}

	/*
	 * Error handle all Exception
	 * 
	 */
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Throwable ex, WebRequest request) {
		return buildResponseEntity(
				new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error", ex.getLocalizedMessage()));
	}

	@ExceptionHandler(PermissionErrorException.class)
	public ResponseEntity<Object> handlePermissionDeny(PermissionErrorException ex, WebRequest request) {
		return buildResponseEntity(
				new ErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", ex.getLocalizedMessage()));
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public final ResponseEntity<Object> handleUserNotFoundException(EntityNotFoundException ex, WebRequest request) {

		return buildResponseEntity(
				new ErrorResponse(HttpStatus.NOT_FOUND, "Entity was not found", ex.getLocalizedMessage()));
	}

	@ExceptionHandler(EmailInvalidException.class)
	public final ResponseEntity<Object> handleEmailInvalid(EmailInvalidException ex, WebRequest request) {

		return buildResponseEntity(
				new ErrorResponse(HttpStatus.CONFLICT, "You entered an incorrect password", ex.getLocalizedMessage()));
	}

	@ExceptionHandler(EmailAlreadyExistException.class)
	public final ResponseEntity<Object> handleEmailAlreadyExist(EmailAlreadyExistException ex, WebRequest request) {

		return buildResponseEntity(new ErrorResponse(HttpStatus.CONFLICT,
				"You have entered the current email address. Enter another email.", ex.getLocalizedMessage()));
	}

	@ExceptionHandler(EntityAlreadyExistException.class)
	public final ResponseEntity<Object> handleEntityExist(EntityAlreadyExistException ex, WebRequest request) {
		return buildResponseEntity(
				new ErrorResponse(HttpStatus.CONFLICT, "Entity already exist with this ID", ex.getLocalizedMessage()));
	}

	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getHttpStatus());
	}
}
