package com.oversoul.security.model;

public interface TokenVerifier {

	public boolean verify(String jti);

}
