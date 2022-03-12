package com.oversoul.security.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Tokens {

	private String token;

	private String refreshToken;

	private String firstName;

	private String lastName;

	private String userName;

	private boolean isSignUpDone;

	private boolean isPwdExpiry;

	private Long roleIds;
	
	private UUID tenantId;

}
