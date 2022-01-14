package com.oversoul.service;

import java.util.Optional;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.oversoul.entity.User;
import com.oversoul.entity.UserRole;
import com.oversoul.repository.UserRepository;
import com.oversoul.repository.UserRoleRepository;
import com.oversoul.util.ApiConstants;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.ApiReturnWithResult;
import com.oversoul.vo.RoleVo;
import com.oversoul.vo.UserVo;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepo;

	private UserRoleRepository userRoleRepo;

	public UserServiceImpl(UserRepository userRepo, UserRoleRepository userRoleRepo) {
		this.userRepo = userRepo;
		this.userRoleRepo = userRoleRepo;
	}

	@Override
	public ApiReturn getProfile() {
		Long userId = Long.parseLong(MDC.get("userId"));
		Optional<User> userDetails = userRepo.findById(userId);
		if (userDetails.isPresent()) {
			User user = userDetails.get();
			UserRole userRole = userRoleRepo.findByUserId(user);
			return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
					new UserVo(user.getFirstName(), user.getLastName(), user.getUserName(),
							new RoleVo(userRole.getId(), userRole.getRoleId().getName())));
		}

		return new ApiReturn(HttpStatus.NOT_FOUND.value(), ApiConstants.Status.FAILED.name(), null);

	}

}
