package com.oversoul.service;

import java.util.Optional;
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

    private final TenantDetailsRepository tenantDetailsRepo;

    private final ObjectMapper objectMapper;

    public TenantServiceImpl(TenantDetailsRepository tenantDetailsRepo, ObjectMapper objectMapper) {
        this.tenantDetailsRepo = tenantDetailsRepo;
        this.objectMapper = objectMapper;
    }

    @Override
    public ApiReturn createTenant(TenantDetailsReq tenantDetailsReq) {
        Long userId = Long.parseLong(MDC.get("userId"));
        Optional<TenantDetails> tenant = tenantDetailsRepo.findByClientNameOrEmail(tenantDetailsReq.getClientName(), tenantDetailsReq.getEmail());
        if (tenant.isPresent()) {
        	return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.FAILED.name(), "Tenant Name/Email already exists");
        }
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

        throw new NotFoundException("Invalid tenant Details");

    }

    @Override
    public ApiReturn getTenants(TenantDetailsReq tenantDetails) {
        if (tenantDetails.getStatus() != null) {
            return getTenantsWithStatus(tenantDetails);
        } else {
            return getTenantsWithOutStatus(tenantDetails);

        }


    }

    private ApiReturnWithResult getTenantsWithOutStatus(TenantDetailsReq tenantDetails) {
        if (tenantDetails.getId() != null && tenantDetails.getClientName() != null) {
            return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
                    tenantDetailsRepo.findByIdAndClientNameLikeIgnoreCase(tenantDetails.getId(), tenantDetails.getClientName()));
        }
        if (tenantDetails.getId() != null) {
            return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
                    tenantDetailsRepo.findById(tenantDetails.getId()));
        }
        if (tenantDetails.getClientName() != null) {
            return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
                    tenantDetailsRepo.findByClientNameLikeIgnoreCase(tenantDetails.getClientName()));
        } else {
            return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
                    tenantDetailsRepo.findAll());
        }
    }

    private ApiReturnWithResult getTenantsWithStatus(TenantDetailsReq tenantDetails) {
        if (tenantDetails.getId() != null && tenantDetails.getClientName() != null) {
            return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
                    tenantDetailsRepo.findByIdAndClientNameLikeIgnoreCaseAndStatus(tenantDetails.getId(), tenantDetails.getClientName(), tenantDetails.getStatus()));
        }
        if (tenantDetails.getId() != null) {
            return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
                    tenantDetailsRepo.findByIdAndStatus(tenantDetails.getId(), tenantDetails.getStatus()));
        }
        if (tenantDetails.getClientName() != null) {
            return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
                    tenantDetailsRepo.findByClientNameLikeIgnoreCaseAndStatus(tenantDetails.getClientName(), tenantDetails.getStatus()));
        } else {
            return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
                    tenantDetailsRepo.findByStatus(tenantDetails.getStatus()));
        }
    }

}
