package com.oversoul.service;

import com.oversoul.entity.Role;
import com.oversoul.entity.User;
import com.oversoul.entity.UserRole;
import com.oversoul.exception.CommonException;
import com.oversoul.projection.UserProjection;
import com.oversoul.repository.*;
import com.oversoul.util.ApiConstants;
import com.oversoul.util.Constants;
import com.oversoul.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    private final UserRoleRepository userRoleRepo;

    private final RoleRepository roleRepo;

    private final TenantDetailsRepository tenantDetailsRepo;

    private final UserMappingRepository userMappingRepo;

    private final UserMappingService userMappingService;

    public UserServiceImpl(UserRepository userRepo, UserRoleRepository userRoleRepo, RoleRepository roleRepo,
                           TenantDetailsRepository tenantDetailsRepo, UserMappingRepository userMappingRepo, UserMappingService userMappingService) {
        this.userRepo = userRepo;
        this.userRoleRepo = userRoleRepo;
        this.roleRepo = roleRepo;
        this.tenantDetailsRepo = tenantDetailsRepo;
        this.userMappingRepo = userMappingRepo;
        this.userMappingService = userMappingService;
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
                            user.getId(),
                            new RoleVo(userRole.getId(), userRole.getRoleId().getName())));
        }

        return new ApiReturn(HttpStatus.NOT_FOUND.value(), ApiConstants.Status.FAILED.name(), null);

    }

    @Override
    public ApiReturn createUser(UserRequest userRequest) throws CommonException {
        if (!tenantDetailsRepo.existsById(userRequest.getTenantId())) {
            throw new CommonException("Invalid Tenant Details");
        }

        User user = null;
        boolean isUpdateUser = false;
        if (userRequest.getId() != null) {
            Optional<User> userObj = userRepo.findById(userRequest.getId());
            if (userObj.isPresent()) {
                isUpdateUser = true;
                user = userObj.get();
            }
        } else if (userRequest.getEmail().trim().length() == 0 || Boolean.TRUE
                .equals(userRepo.existsByEmailAndTenantId(userRequest.getEmail(), userRequest.getTenantId()))) {
            throw new CommonException("Email already exists in the system");
        }

        Optional<Role> role = roleRepo.findById(userRequest.getRole());

        if (!role.isPresent()) {
            throw new CommonException("Invalid User Role");

        }
        Long loggedInUserId = Long.parseLong(MDC.get("userId"));

        if (user == null) {
            user = new User();
            user.setCreatedBy(loggedInUserId);
            user.setUserName(userRequest.getUserName());
        }
        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setMobile(userRequest.getMobile());
        user.setTenantId(userRequest.getTenantId());
        user.setActive(userRequest.isActive());
        user = userRepo.save(user);
        if (isUpdateUser) {
            UserRole ur = userRoleRepo.findByUserId(user.getId());
            ur.setRoleId(role.get());
            userRoleRepo.save(ur);
        } else {
            UserRole ur = new UserRole();
            ur.setRoleId(role.get());
            ur.setUserId(user.getId());
            userRoleRepo.save(ur);
        }


        UserMapVo map = new UserMapVo();
        map.setFromUserId(userRequest.getCoachId());
        List<Long> employeeIds = new ArrayList<>();
        employeeIds.add(user.getId());
        map.setToUserId(employeeIds);
        map.setFromRoleId((long) Constants.COACH);
        map.setToRoleId((long) Constants.EMPLOYEE);
        log.info("before mapping user with coach");
        userMappingService.userMapping(map);
        log.info("mapping completed");
//        UserMapping existingMapedUser = userMappingRepo.findByManagerIdAndCoachIdAndEmployeeId(userRequest.getManagerId(), userRequest.getCoachId(), user.getId());
//        if (existingMapedUser == null) {
//            userMappingRepo.save(new UserMapping(loggedInUserId, userRequest.getCoachId(), user.getId(), loggedInUserId));
//        }

        return new ApiReturn(HttpStatus.CREATED.value(), ApiConstants.Status.SUCCESS.name(),
                "User created Successfully");

    }


    @Override
    public ApiReturn getUserList(UserListReq userListReq) {
        List<UserProjection> userList = null;
        if (userListReq != null && userListReq.getTenantId() != null && userListReq.getRoleId() != null && userListReq.getRoleId() != 0) {
            userList = userRepo.findByTenantIdAndRoleId_Id(userListReq.getTenantId(), userListReq.getRoleId());
        } else if (userListReq != null && userListReq.getTenantId() != null) {
            userList = userRepo.findByTenantId(userListReq.getTenantId());
        } else if (userListReq != null && userListReq.getRoleId() != null && userListReq.getRoleId() != 0) {
            userList = userRepo.findByRoleId_Id(userListReq.getRoleId());
        } else {
            userList = userRepo.findAllWithTenantName();
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

    @Override
    public ApiReturn getAssignedUserDetails(Long userId) throws CommonException {
        UserRole userRole = userRoleRepo.findByUserId(userId);
        Optional.ofNullable(userRole).orElseThrow(() -> new CommonException("No role assigned"));
        UserDetails userDetails = new UserDetails();
        Long coachId = null;
        Long managerId = null;
        if (userRole.getRoleId().getId() == Constants.EMPLOYEE) {
            coachId = userMappingRepo.findByEmployeeIdAndManagerIdIsNull(userId);
            managerId = userMappingRepo.findByCoachIdAndEmployeeIdIsNull(coachId);
        } else if (userRole.getRoleId().getId() == Constants.COACH) {
            managerId = userMappingRepo.findByCoachIdAndEmployeeIdIsNull(userId);
        }
        userDetails.setAssignedCoachId(coachId);
        userDetails.setAssignedManagerId(managerId);
        return new ApiReturnWithResult(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
                userDetails);
    }

}
