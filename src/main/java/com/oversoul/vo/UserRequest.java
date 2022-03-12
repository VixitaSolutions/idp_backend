package com.oversoul.vo;

import java.util.UUID;

import javax.annotation.Nonnull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

	
	private Long id;
	private String firstName;
	private String lastName;
	private String userName;
	private Long role;
	@Nonnull
	private String email;
	private String mobile;
	@Nonnull
	private UUID tenantId;
	private boolean active=true;
	private Long coachId;
	private Long managerId;

}
