package com.oversoul.security.exceptions;

import org.springframework.security.authentication.AuthenticationServiceException;

public class ConflictException extends AuthenticationServiceException {
	private static final long serialVersionUID = 3705043083010304496L;

	public ConflictException(String msg) {
		super(msg);
	}
}
