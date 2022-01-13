package com.oversoul.security.exceptions;

import org.springframework.security.authentication.AuthenticationServiceException;

public class InvalidOAuthAccess extends AuthenticationServiceException {

	private static final long serialVersionUID = 3705043083010304496L;

	public InvalidOAuthAccess(String msg) {
		super(msg);
	}
}
