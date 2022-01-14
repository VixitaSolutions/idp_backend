package com.oversoul.exception;

import lombok.Data;

@Data
public class NoResultException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;

	public NoResultException(String message) {
		this.message = message;
	}

}
