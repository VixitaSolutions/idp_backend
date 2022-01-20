package com.oversoul.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.oversoul.entity.Role;
import com.oversoul.entity.UserMapping;
import com.oversoul.exception.CommonException;
import com.oversoul.repository.RoleRepository;
import com.oversoul.repository.UserMappingRepository;
import com.oversoul.repository.UserRepository;
import com.oversoul.repository.UserRoleRepository;
import com.oversoul.util.ApiConstants;
import com.oversoul.util.Constants;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.ApiReturnWithResult;
import com.oversoul.vo.UserMapVo;

@Service
public class UserMappingServiceImpl implements UserMappingService {

	private UserMappingRepository userMappingRepo;

	private UserRepository userRepo;

	private UserRoleRepository userRoleRepo;

	private RoleRepository roleRepo;

	public UserMappingServiceImpl(UserMappingRepository userMappingRepo, UserRepository userRepo,
			UserRoleRepository userRoleRepo, RoleRepository roleRepo) {
		this.userMappingRepo = userMappingRepo;
		this.userRepo = userRepo;
		this.userRoleRepo = userRoleRepo;
		this.roleRepo = roleRepo;
	}

	@Override
	public ApiReturn userMapping(UserMapVo userMapVo) throws CommonException {

		Long loggedInUserId = Long.parseLong(MDC.get("userId"));

		List<UserMapping> mapList = new ArrayList<UserMapping>();
		if (userMapVo.getFromRoleId() == (Constants.MANAGER) && userMapVo.getToRoleId() == (Constants.COACH)) {
			Long fromUserId = userMapVo.getFromUserId();
			for (Long toUserId : userMapVo.getToUserId()) {
				if (!userMappingRepo.existsByManagerIdAndCoachId(fromUserId, toUserId)) {
					mapList.add(new UserMapping(fromUserId, toUserId, null, loggedInUserId));
				}
			}
		} else if (userMapVo.getFromRoleId() == (Constants.MANAGER)
				&& userMapVo.getToRoleId() == (Constants.EMPLOYEE)) {
			Long fromUserId = userMapVo.getFromUserId();
			for (Long toUserId : userMapVo.getToUserId()) {
				if (!userMappingRepo.existsByManagerIdAndEmployeeId(fromUserId, toUserId)) {
					mapList.add(new UserMapping(fromUserId, null, toUserId, loggedInUserId));
				}
			}
		} else if (userMapVo.getFromRoleId() == (Constants.COACH) && userMapVo.getToRoleId() == (Constants.EMPLOYEE)) {
			Long fromUserId = userMapVo.getFromUserId();
			for (Long toUserId : userMapVo.getToUserId()) {
				if (!userMappingRepo.existsByCoachIdAndEmployeeId(fromUserId, toUserId)) {
					mapList.add(new UserMapping(null, fromUserId, toUserId, loggedInUserId));
				}
			}
		} else {
			throw new CommonException("Invalid role Mapping");
		}
		userMappingRepo.saveAll(mapList);

		return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(), "Mapped Successfully");

	}

	@Override
	public ApiReturn deLinkMappling(UserMapVo userMapVo) {
		return null;
	}

	@Override
	public ApiReturn getAllotedEmployeesByCoachId(Long employeeTypeId, Long userId) throws CommonException {
		List<Long> employeeIds = null;
		if (employeeTypeId.equals(Long.valueOf(Constants.EMPLOYEE))) {
			Optional<Role> role = roleRepo.findById(Long.valueOf(Constants.COACH));

			if (userRoleRepo.existsByUserIdAndRoleId(userId, role.get())) {

				employeeIds = userMappingRepo.findEmployeeIdByCoachId(userId);

			}
		} else if (employeeTypeId.equals(Long.valueOf(Constants.COACH))) {
			Optional<Role> role = roleRepo.findById(Long.valueOf(Constants.MANAGER));

			if (userRoleRepo.existsByUserIdAndRoleId(userId, role.get())) {
				employeeIds = userMappingRepo.findCoachIdByManagerId(userId);
			}

		} else {
			throw new CommonException("Please Provide Coach Details");
		}
		if (employeeIds != null && !employeeIds.isEmpty()) {

			return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
					userRepo.findByIdIn(employeeIds));
		}
		return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(), "User Not found");
	}

}
