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
		StandardErrorMessage message = new StandardErrorMessage();
		message.setTimestamp(Instant.now());
		message.setStatus(HttpStatus.NOT_FOUND.value());
		message.setError("Resource not found");
		message.setMessage(e.getMessage());
		message.setPath(r.getRequestURI());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
	}
}
