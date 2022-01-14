package com.oversoul.exception;

import java.util.List;

import lombok.Data;

@Data
public class BindingResultErrorException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private List<String> bindingErrors;

	public BindingResultErrorException(List<String> errors) {
		this.bindingErrors = errors;
	}
}
