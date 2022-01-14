package com.oversoul.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oversoul.service.LoginService;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.UserSignUpVo;

@RestController
@RequestMapping("/api/v1/login/")
public class LoginController {

	private LoginService loginService;

	public LoginController(LoginService loginService) {
		super();
		this.loginService = loginService;
	}

	@PostMapping("send/otp")
	public ApiReturn sendOtp(@RequestBody UserSignUpVo userSignUpVo) {
		return loginService.sendOtp(userSignUpVo);
	}

	@PostMapping("verify/otp")
	public ApiReturn verifyOtp(@RequestBody UserSignUpVo userSignUpVo) {
		return loginService.verifyOtp(userSignUpVo);
	}

	@PostMapping("send/temporaryPassword")
	public ApiReturn sendTemporaryPassword(@RequestBody UserSignUpVo userSignUpVo) {
		return loginService.sendTemporaryPassword(userSignUpVo);
	}

	@PostMapping("verify/temporaryPassword")
	public ApiReturn verifyTemporaryPassword(@RequestBody UserSignUpVo userSignUpVo) {
		return loginService.verifyTemporaryPassword(userSignUpVo);
	}

	@PostMapping("create/password")
	public ApiReturn createPassword(@RequestBody UserSignUpVo userSignUpVo) {
		return loginService.createPassword(userSignUpVo);
	}

}
