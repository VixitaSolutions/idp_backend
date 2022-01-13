package com.oversoul.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

	@GetMapping("health-check")
	public String healthCheck() {
		return "it is up";
	}

	@GetMapping("api/health-check")
	public String apiHelthCheck() {
		return "api/health-check is it up";
	}

}
