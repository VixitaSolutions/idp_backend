package com.oversoul.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.oversoul.entity.Role;
import com.oversoul.entity.User;
import com.oversoul.entity.UserRole;
import com.oversoul.exception.CommonException;
import com.oversoul.repository.RoleRepository;
import com.oversoul.repository.TenantDetailsRepository;
import com.oversoul.repository.UserRepository;
import com.oversoul.repository.UserRoleRepository;
import com.oversoul.util.ApiConstants;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.ApiReturnWithResult;
import com.oversoul.vo.RoleVo;
import com.oversoul.vo.UserRequest;
import com.oversoul.vo.UserVo;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepo;

	private UserRoleRepository userRoleRepo;

	private RoleRepository roleRepo;

	private TenantDetailsRepository tenantDetailsRepo;

	public UserServiceImpl(UserRepository userRepo, UserRoleRepository userRoleRepo, RoleRepository roleRepo,
			TenantDetailsRepository tenantDetailsRepo) {
		this.userRepo = userRepo;
		this.userRoleRepo = userRoleRepo;
		this.roleRepo = roleRepo;
		this.tenantDetailsRepo = tenantDetailsRepo;
	}

	@Override
	public ApiReturn getProfile() {
		Long userId = Long.parseLong(MDC.get("userId"));
		Optional<User> userDetails = userRepo.findById(userId);
		if (userDetails.isPresent()) {
			User user = userDetails.get();
			UserRole userRole = userRoleRepo.findByUserId(user.getId());
			return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
					new UserVo(user.getFirstName(), user.getLastName(), user.getUserName(),
							new RoleVo(userRole.getId(), userRole.getRoleId().getName())));
		}

		return new ApiReturn(HttpStatus.NOT_FOUND.value(), ApiConstants.Status.FAILED.name(), null);

	}

	@Override
	public ApiReturn createUser(UserRequest userRequest) throws CommonException {
		if (userRequest.getTenantId() == null || !tenantDetailsRepo.existsById(userRequest.getTenantId())) {
			throw new CommonException("Invalid Tenant Details");

		}
		if (userRequest.getEmail() == null || userRequest.getEmail().trim().length() == 0 || Boolean.TRUE
				.equals(userRepo.existsByEmailAndTenantId(userRequest.getEmail(), userRequest.getTenantId()))) {
			throw new CommonException("Email already exists in the system");
		}

		Optional<Role> role = roleRepo.findById(userRequest.getRole());

		if (!role.isPresent()) {
			throw new CommonException("Invalid User Role");

		}
		Long loggedInUserId = Long.parseLong(MDC.get("userId"));
		User user = new User();
		user.setCreatedBy(loggedInUserId);
		user.setEmail(userRequest.getEmail());
		user.setFirstName(userRequest.getFirstName());
		user.setLastName(userRequest.getLastName());
		user.setUserName(userRequest.getUserName());
		user.setMobile(userRequest.getMobile());
		user.setTenantId(userRequest.getTenantId());
		user = userRepo.save(user);

		UserRole ur = new UserRole();
		ur.setRoleId(role.get());
		ur.setUserId(user.getId());
		userRoleRepo.save(ur);

		return new ApiReturn(HttpStatus.CREATED.value(), ApiConstants.Status.SUCCESS.name(),
				"User created Succesfully");

	}
	
	
	@Override
	public ApiReturn getUserList(UUID clientId) {
		List<User> userList = userRepo.findByTenantId(clientId);
		return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
					userList);
	}

}
