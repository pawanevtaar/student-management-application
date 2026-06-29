package com.stm.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.stm.dto.ErrorResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CourseNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleCourseNotFound(
	        CourseNotFoundException ex) {

	    ErrorResponse error = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.NOT_FOUND.value(),
	            "Not Found",
	            ex.getMessage()
	    );

	    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
}