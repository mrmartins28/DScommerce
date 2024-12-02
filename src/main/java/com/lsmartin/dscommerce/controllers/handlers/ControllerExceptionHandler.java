package com.lsmartin.dscommerce.controllers.handlers;

import java.time.Instant;
import java.util.Iterator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.lsmartin.dscommerce.dto.CustomError;
import com.lsmartin.dscommerce.dto.FieldMessage;
import com.lsmartin.dscommerce.dto.ValidationError;
import com.lsmartin.dscommerce.services.exceptions.DataBaseException;
import com.lsmartin.dscommerce.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<CustomError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request){	
		HttpStatus status = HttpStatus.NOT_FOUND;
		CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
		
		
	}
	

	@ExceptionHandler(DataBaseException.class)
	public ResponseEntity<CustomError> dataBase(DataBaseException e, HttpServletRequest request){	
		HttpStatus status = HttpStatus.BAD_REQUEST;
		CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CustomError> MethodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request){	
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		ValidationError err = new  ValidationError(Instant.now(), status.value(), "Dados inválidos", request.getRequestURI());
		for (FieldError f : e.getBindingResult().getFieldErrors()) {
			
			err.addErrors(f.getField(), f.getDefaultMessage());
			
		}
		return ResponseEntity.status(status).body(err);
	}

}
