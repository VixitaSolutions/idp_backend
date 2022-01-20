package com.oversoul.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oversoul.exception.CommonException;
import com.oversoul.service.UserService;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.UserRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/user/")
@SecurityRequirement(name = "Authorization")
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	@Operation(summary = "This is to fetch user profile by token")
	@GetMapping("profile")
	public ApiReturn getProfile() {
		return userService.getProfile();

	}

	@PostMapping("create")
	public ApiReturn createUser(@RequestBody UserRequest userRequest) throws CommonException {
		return userService.createUser(userRequest);
	}

}
