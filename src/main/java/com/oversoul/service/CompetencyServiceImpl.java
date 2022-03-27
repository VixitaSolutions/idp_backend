package com.oversoul.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.oversoul.repository.CompetencyRepository;
import com.oversoul.util.ApiConstants;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.ApiReturnWithResult;

@Service
public class CompetencyServiceImpl implements CompetencyService {

	private final CompetencyRepository competencyRepo;

	CompetencyServiceImpl(CompetencyRepository competencyRepo) {
		this.competencyRepo = competencyRepo;
	}
	@Override
	public ApiReturn getCompetencies(UUID tenantId) {
		return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
				competencyRepo.findByTenantId(tenantId));
	}

}
