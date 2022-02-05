package com.oversoul.controller;

import com.oversoul.service.TenantService;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.TenantDetailsReq;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tenant/")
@SecurityRequirement(name = "Authorization")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @PostMapping("create")
    public ApiReturn createTenant(@RequestBody TenantDetailsReq tenantDetails) {
        return tenantService.createTenant(tenantDetails);
    }

    @PostMapping("update")
    public ApiReturn updateTenant(@RequestBody TenantDetailsReq tenantDetails) throws Exception {
        return tenantService.updateTenant(tenantDetails);
    }

    @PostMapping("get")
    public ApiReturn getTenants(@RequestBody TenantDetailsReq tenantDetails) {
        return tenantService.getTenants(tenantDetails);
    }

}
