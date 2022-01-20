package com.oversoul.service;

import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oversoul.entity.TenantDetails;
import com.oversoul.exception.NotFoundException;
import com.oversoul.repository.TenantDetailsRepository;
import com.oversoul.util.ApiConstants;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.ApiReturnWithResult;
import com.oversoul.vo.TenantDetailsReq;

@Service
public class TenantServiceImpl implements TenantService {

	private TenantDetailsRepository tenantDetailsRepo;

	private ObjectMapper objectMapper;

	public TenantServiceImpl(TenantDetailsRepository tenantDetailsRepo, ObjectMapper objectMapper) {
		this.tenantDetailsRepo = tenantDetailsRepo;
		this.objectMapper = objectMapper;
	}

	@Override
	public ApiReturn createTenant(TenantDetailsReq tenantDetailsReq) {
		Long userId = Long.parseLong(MDC.get("userId"));
		TenantDetails tenantDetails = objectMapper.convertValue(tenantDetailsReq, TenantDetails.class);
		tenantDetails.setCreatedBy(userId);
		UUID tenantId = tenantDetailsRepo.save(tenantDetails).getId();
		return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(), tenantId);

	}

	@Override
	public ApiReturn updateTenant(TenantDetailsReq tenantDetailsReq) throws Exception {
		Long userId = Long.parseLong(MDC.get("userId"));
		if (tenantDetailsReq.getId() != null && tenantDetailsRepo.existsById(tenantDetailsReq.getId())) {
			TenantDetails tenantDetails = objectMapper.convertValue(tenantDetailsReq, TenantDetails.class);
			tenantDetails.setUpdatedBy(userId);
			UUID tenantId = tenantDetailsRepo.save(tenantDetails).getId();
			return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(), tenantId);
		}

		throw new NotFoundException("Invalid tenant Deatils");

	}

	@Override
	public ApiReturn getTenants() {
		return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
				tenantDetailsRepo.findAll());

	}

}
