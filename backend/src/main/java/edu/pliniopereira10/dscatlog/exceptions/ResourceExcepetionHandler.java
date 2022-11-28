package edu.pliniopereira10.dscatlog.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceExcepetionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardErrorMessage> resourceNotFound(ResourceNotFoundException e, HttpServletRequest r){
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardErrorMessage message = new StandardErrorMessage();
		message.setTimestamp(Instant.now());
		message.setStatus(status.value());
		message.setError("Resource not found");
		message.setMessage(e.getMessage());
		message.setPath(r.getRequestURI());
		
		return ResponseEntity.status(status).body(message);
	}
	
	@ExceptionHandler(DataBaseException.class)
	public ResponseEntity<StandardErrorMessage> database(DataBaseException e, HttpServletRequest r){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardErrorMessage message = new StandardErrorMessage();
		message.setTimestamp(Instant.now());
		message.setStatus(status.value());
		message.setError("Resource not found");
		message.setMessage(e.getMessage());
		message.setPath(r.getRequestURI());
		
		return ResponseEntity.status(status).body(message);
	}
}
