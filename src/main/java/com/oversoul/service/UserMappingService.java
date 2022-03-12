package com.oversoul.service;

import com.oversoul.exception.CommonException;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.UserMapVo;

import java.util.UUID;

public interface UserMappingService {

	ApiReturn userMapping(UserMapVo userMapVo) throws CommonException;

	ApiReturn deLinkMapping(UserMapVo userMapVo) throws CommonException;

	ApiReturn getAllocatedEmployeesByCoachId(Long employeeId, Long coachId) throws CommonException;

	ApiReturn getUnAllocatedEmployees(UUID tenantId) throws CommonException;
}
