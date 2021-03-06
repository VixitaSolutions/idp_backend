package com.oversoul.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.SpringSecurityCoreVersion;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsernamePasswordAuthentication extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	private transient Object loginType;
	private transient Object authProvider;
	private transient Object authId;
	private transient Object accessToken;
	private transient Object phone;
	private transient Object tenant;

	private long appId;

	public UsernamePasswordAuthentication(Object principal, Object credentials, Object loginType, Object tenant) {
		super(principal, credentials);
		this.loginType = loginType;
		this.tenant = tenant;
		setAuthenticated(false);
	}

	public UsernamePasswordAuthentication(Object principal, Object credentials, Object loginType, Object tenant, Object authProvider,
			Object authId) {
		super(principal, credentials);
		this.loginType = loginType;
		this.authProvider = authProvider;
		this.authId = authId;
		this.tenant = tenant;
		setAuthenticated(false);
	}

	// add url
	public UsernamePasswordAuthentication(Object principal, Object credentials, Object loginType, Object tenant, Object authProvider,
			Object authId, Object accessToken, Object phone) {
		super(principal, credentials);
		this.loginType = loginType;
		this.authProvider = authProvider;
		this.authId = authId;
		this.accessToken = accessToken;
		this.phone = phone;
		this.tenant = tenant;
		setAuthenticated(false);
	}

	public UsernamePasswordAuthentication(Object loginType, Object tenant) {
		super(null, null);
		this.loginType = loginType;
		this.tenant = tenant;
		setAuthenticated(false);
	}

}
