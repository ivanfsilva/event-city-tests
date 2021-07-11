package com.devsuperior.bds04.controllers.exceptions;

import com.devsuperior.bds04.services.exceptions.DatabaseException;
import com.devsuperior.bds04.services.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler({MethodArgumentNotValidException.class})
	public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

		ValidationError error = new ValidationError();
		error.setTimeStamp(Instant.now());
		error.setStatus(status.value());
		error.setError("Validation Exception");
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());

		for (FieldError f : e.getBindingResult().getFieldErrors()) {
			error.addError(f.getField(), f.getDefaultMessage());
		}

		return ResponseEntity.status(status).body(error);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;

		StandardError error = new StandardError();
		error.setTimeStamp(Instant.now());
		error.setStatus(status.value());
		error.setError("Recurso não encontrado");
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());

		return ResponseEntity.status(status).body(error);
	}

	@ExceptionHandler({DatabaseException.class})
	public ResponseEntity<StandardError> database(DatabaseException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;

		StandardError error = new StandardError();
		error.setTimeStamp(Instant.now());
		error.setStatus(status.value());
		error.setError("Violação de integridade");
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());

		return ResponseEntity.status(status).body(error);
	}



}
