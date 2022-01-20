package com.oversoul.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oversoul.service.TenantService;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.TenantDetailsReq;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/tenant/")
@SecurityRequirement(name = "Authorization")
public class TenantController {

	private TenantService tenantService;

	public TenantController(TenantService tenantService) {
		this.tenantService = tenantService;
	}

	@PostMapping("create")
	private ApiReturn createTenant(@RequestBody TenantDetailsReq tenantDetails) {
		return tenantService.createTenant(tenantDetails);
	}

	@PostMapping("update")
	private ApiReturn updateTenant(@RequestBody TenantDetailsReq tenantDetails) throws Exception {
		return tenantService.updateTenant(tenantDetails);
	}

	@GetMapping("get")
	private ApiReturn getTenants() {
		return tenantService.getTenants();
	}

}
