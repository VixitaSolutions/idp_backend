package com.oversoul.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oversoul.exception.CommonException;
import com.oversoul.service.UserMappingService;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.UserMapVo;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/usermap")
@SecurityRequirement(name = "Authorization")
public class UserMappingController {

	private UserMappingService userMappingService;

	public UserMappingController(UserMappingService userMappingService) {
		this.userMappingService = userMappingService;
	}

	@PutMapping("link")
	public ApiReturn userMapping(@RequestBody UserMapVo userMapVo) throws CommonException {
		return userMappingService.userMapping(userMapVo);
	}

	@PutMapping("deLink")
	public ApiReturn deLinkMapping(@RequestBody UserMapVo userMapVo) {
		return userMappingService.deLinkMapping(userMapVo);
	}

	@GetMapping("allocated/{employeeTypeId}")
	public ApiReturn getAllocatedEmployees(@PathVariable Long employeeTypeId, @RequestParam Long coachId)
			throws CommonException {
		return userMappingService.getAllocatedEmployeesByCoachId(employeeTypeId, coachId);
	}
}
