package com.oversoul.vo;

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
public class UserVo {

	private String firstName;
	private String lastName;
	private String userName;
	private Long userId;
	private RoleVo role;

}
