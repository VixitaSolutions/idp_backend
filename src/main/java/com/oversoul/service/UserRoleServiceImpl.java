package com.oversoul.service;

import com.oversoul.entity.Role;
import com.oversoul.entity.UserRole;
import com.oversoul.exception.CommonException;
import com.oversoul.repository.RoleRepository;
import com.oversoul.repository.UserRepository;
import com.oversoul.repository.UserRoleRepository;
import com.oversoul.util.ApiConstants;
import com.oversoul.vo.ApiReturn;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepo;

    private final UserRepository userRepo;

    private final RoleRepository roleRepo;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepo, RoleRepository roleRepo, UserRepository userRepo) {
        this.userRoleRepo = userRoleRepo;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;

    }

    @Override
    public ApiReturn addRole(Long userId, Long roleId) throws CommonException {
        Role role = roleRepo.findById(roleId).orElseThrow(() -> new CommonException("Role Not Found"));
        if (userRepo.existsById(userId)) {
            UserRole userRole = userRoleRepo.findByUserId(userId);
            if (userRole == null) {
                userRole = new UserRole();
                userRole.setRoleId(role);
                userRole.setUserId(userId);
            } else {
                userRole.setRoleId(role);
            }
            userRoleRepo.save(userRole);
            return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
                    "User Role Updated");
        }
        throw new CommonException("User Not Found");

    }
}
