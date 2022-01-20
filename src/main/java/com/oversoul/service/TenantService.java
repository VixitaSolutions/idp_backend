package com.oversoul.service;

import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.TenantDetailsReq;

public interface TenantService {

	ApiReturn createTenant(TenantDetailsReq tenantDetails);

	ApiReturn getTenants();

	ApiReturn updateTenant(TenantDetailsReq tenantDetails) throws Exception;

}
