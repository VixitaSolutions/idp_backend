package com.oversoul.service;

import java.util.UUID;

import com.oversoul.vo.ApiReturn;

public interface CompetencyService {

    ApiReturn getCompetencies(UUID tenantId);
}
