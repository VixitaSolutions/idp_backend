package com.oversoul.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.SpringSecurityCoreVersion;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsernamePasswordAuthentication extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	private Object loginType;
	private Object authProvider;
	private Object authId;
	private Object accessToken;
	private Object phone;

	private long appId;

	public UsernamePasswordAuthentication(Object principal, Object credentials, Object loginType) {
		super(principal, credentials);
		this.loginType = loginType;
		setAuthenticated(false);
	}

	public UsernamePasswordAuthentication(Object principal, Object credentials, Object loginType, Object authProvider,
			Object authId) {
		super(principal, credentials);
		this.loginType = loginType;
		this.authProvider = authProvider;
		this.authId = authId;
		setAuthenticated(false);
	}

	// add url
	public UsernamePasswordAuthentication(Object principal, Object credentials, Object loginType, Object authProvider,
			Object authId, Object accessToken, Object phone) {
		super(principal, credentials);
		this.loginType = loginType;
		this.authProvider = authProvider;
		this.authId = authId;
		this.accessToken = accessToken;
		this.phone = phone;
		setAuthenticated(false);
	}

	public UsernamePasswordAuthentication(Object loginType) {
		super(null, null);
		this.loginType = loginType;
		setAuthenticated(false);
	}

}
