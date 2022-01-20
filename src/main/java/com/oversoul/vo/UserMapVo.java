package com.oversoul.vo;

import java.util.List;

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
public class UserMapVo {

	private Long fromRoleId;

	private Long fromUserId;

	private Long toRoleId;

	private List<Long> toUserId;

}
