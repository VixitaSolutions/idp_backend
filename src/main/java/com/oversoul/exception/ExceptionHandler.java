package com.oversoul.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.oversoul.util.ApiConstants;
import com.oversoul.vo.ApiReturn;

@ControllerAdvice
public class ExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

	@org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
	public ResponseEntity<ApiReturn> internalExceptionHandler(Exception ex, HttpServletRequest request) {
		ApiReturn api = new ApiReturn(HttpStatus.INTERNAL_SERVER_ERROR.value(), ApiConstants.Status.FAILED.name(),
				HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		log.error("ERROR,{}|{}", api, ex);
		return new ResponseEntity<ApiReturn>(api, HttpStatus.OK);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ApiReturn> notFound(NotFoundException ex) {
		ApiReturn api = new ApiReturn(HttpStatus.NOT_FOUND.value(), ApiConstants.Status.FAILED.name(), ex.getMessage());
		log.error("ERROR,{},{}", api, ex);
		return new ResponseEntity<ApiReturn>(api, HttpStatus.OK);
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(InvalidDataException.class)
	public ResponseEntity<ApiReturn> invalidData(InvalidDataException ex) {
		ApiReturn api = new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.FAILED.name(), ex.getMessage());
		log.error("ERROR,{},{}", api, ex);
		return new ResponseEntity<ApiReturn>(api, HttpStatus.OK);
	}

}
