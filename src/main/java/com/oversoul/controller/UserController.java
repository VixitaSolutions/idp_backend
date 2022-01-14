package com.oversoul.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oversoul.service.UserService;
import com.oversoul.vo.ApiReturn;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	@Operation(security = { @SecurityRequirement(name = "Authorization") })
	@GetMapping("/profile")
	public ApiReturn getProfile() {
		return userService.getProfile();

	}

}
