package com.oversoul.controller;

import java.util.Optional;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oversoul.service.LoginService;
import com.oversoul.util.ApiConstants;
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
	public ApiReturn sendOtp(@RequestBody UserSignUpVo userSignUpVo) throws Exception {
		return loginService.sendOtp(userSignUpVo);
	}

	@PostMapping("verify/otp")
	public ApiReturn verifyOtp(@RequestBody UserSignUpVo userSignUpVo) {
		return loginService.verifyOtp(userSignUpVo);
	}

	@PostMapping("send/temporaryPassword")
	public ApiReturn sendTemporaryPassword(@RequestHeader(name = "tenant-id", required = false) String tenantId, @RequestBody UserSignUpVo userSignUpVo) {
		if (!Optional.ofNullable(tenantId).isPresent()) {
			return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.FAILED.name(), "Invalid Tenant-ID");
		}
		MDC.put("tenantId", tenantId);
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
