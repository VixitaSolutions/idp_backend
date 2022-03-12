package com.oversoul.security.model;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class UserContext {
	private final String username;
	private final String userId;
	private final UUID tenantId;
	private final LoginType loginType;
	private final List<GrantedAuthority> authorities;

	private UserContext(String username, String userId, LoginType loginType, List<GrantedAuthority> authorities, UUID tenantId) {
		this.username = username;
		this.userId = userId;
		this.loginType = loginType;
		this.authorities = authorities;
		this.tenantId = tenantId;
	}

	public static UserContext create(String username, String userId, LoginType loginType,
			List<GrantedAuthority> authorities, UUID tenantId) {
		if (StringUtils.isBlank(username))
			throw new IllegalArgumentException("Username is blank: " + username);
		return new UserContext(username, userId, loginType, authorities, tenantId);
	}

	public static UserContext createContext(String username, String userId, LoginType loginType,
			List<GrantedAuthority> authorities, UUID tenantId) {
		if (StringUtils.isBlank(userId))
			throw new IllegalArgumentException("UserId is blank: " + userId);
		return new UserContext(username, userId, loginType, authorities, tenantId);
	}

}
