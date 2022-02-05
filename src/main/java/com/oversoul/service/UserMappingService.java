package com.oversoul.service;

import com.oversoul.exception.CommonException;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.UserMapVo;

public interface UserMappingService {

	ApiReturn userMapping(UserMapVo userMapVo) throws CommonException;

	ApiReturn deLinkMapping(UserMapVo userMapVo);

	ApiReturn getAllocatedEmployeesByCoachId(Long employeeId, Long coachId) throws CommonException;

}
