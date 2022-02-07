package com.oversoul.service;

import com.oversoul.entity.Role;
import com.oversoul.entity.User;
import com.oversoul.entity.UserRole;
import com.oversoul.exception.CommonException;
import com.oversoul.repository.RoleRepository;
import com.oversoul.repository.TenantDetailsRepository;
import com.oversoul.repository.UserRepository;
import com.oversoul.repository.UserRoleRepository;
import com.oversoul.util.ApiConstants;
import com.oversoul.vo.*;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    private final UserRoleRepository userRoleRepo;

    private final RoleRepository roleRepo;

    private final TenantDetailsRepository tenantDetailsRepo;

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
        if (!tenantDetailsRepo.existsById(userRequest.getTenantId())) {
            throw new CommonException("Invalid Tenant Details");

        }
        if (userRequest.getEmail().trim().length() == 0 || Boolean.TRUE
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
                "User created Successfully");

    }


    @Override
    public ApiReturn getUserList(UserListReq userListReq) {
        List<User> userList = null;
        if (userListReq != null && userListReq.getTenantId() != null && userListReq.getRoleId() != null) {
            userList = userRepo.findByTenantIdAndRoleId_Id(userListReq.getTenantId(), userListReq.getRoleId());
        } else if (userListReq != null && userListReq.getTenantId() != null) {
            userList = userRepo.findByTenantId(userListReq.getTenantId());
        } else if (userListReq != null && userListReq.getRoleId() != null) {
            userList = userRepo.findByRoleId_Id(userListReq.getRoleId());
        } else {
            userList = userRepo.findAll();
        }

        return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
                userList);
    }

    @Override
    public ApiReturn changeStatus(boolean status, Long userId) throws CommonException {
        User user = userRepo.findById(userId).orElseThrow(() -> new CommonException("User Not Found"));
        user.setActive(status);
        userRepo.save(user);
        return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
                "User Status Updated");
    }

}
