package com.oversoul.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oversoul.security.model.LoginType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model intended to be used for AJAX based authentication.
 */

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
	private String username;
	private String password;
	private LoginType loginType;
	private String firstName;
	private String lastName;
	private String accessToken;

	@JsonCreator
	public LoginRequest(@JsonProperty("username") String username, @JsonProperty("password") String password,
			@JsonProperty("loginType") LoginType loginType, @JsonProperty("at") String accessToken) {
		this.username = username;
		this.password = password;
		this.loginType = loginType;
		this.accessToken = accessToken;
	}

}
