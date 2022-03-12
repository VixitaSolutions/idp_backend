package com.oversoul.service;

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
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserMappingServiceImpl implements UserMappingService {

    private final UserMappingRepository userMappingRepo;

    private final UserRepository userRepo;

    private final UserRoleRepository userRoleRepo;

    private final RoleRepository roleRepo;

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

        List<UserMapping> mapList = new ArrayList<>();
        Long fromUserId = userMapVo.getFromUserId();
        if (userMapVo.getFromRoleId() == (Constants.MANAGER) && userMapVo.getToRoleId() == (Constants.COACH)) {
            for (Long toUserId : userMapVo.getToUserId()) {
                if (!userMappingRepo.existsByManagerIdAndCoachId(fromUserId, toUserId)) {
                    mapList.add(new UserMapping(fromUserId, toUserId, null, loggedInUserId));
                } else {
                	UserMapping um = userMappingRepo.findByManagerIdAndCoachId(fromUserId, toUserId);
                	um.setActive(true);
                	um.setDeLinkedBy(null);
                	mapList.add(um);
                }
            }
        } else if (userMapVo.getFromRoleId() == (Constants.MANAGER)
                && userMapVo.getToRoleId() == (Constants.EMPLOYEE)) {
            for (Long toUserId : userMapVo.getToUserId()) {
                if (!userMappingRepo.existsByManagerIdAndEmployeeId(fromUserId, toUserId)) {
                    mapList.add(new UserMapping(fromUserId, null, toUserId, loggedInUserId));
                } else {
                	UserMapping um = userMappingRepo.findByManagerIdAndCoachId(fromUserId, toUserId);
                	um.setActive(true);
                	um.setDeLinkedBy(null);
                	mapList.add(um);
                }
            }
        } else if (userMapVo.getFromRoleId() == (Constants.COACH) && userMapVo.getToRoleId() == (Constants.EMPLOYEE)) {
            for (Long toUserId : userMapVo.getToUserId()) {
                if (!userMappingRepo.existsByCoachIdAndEmployeeId(fromUserId, toUserId)) {
                    mapList.add(new UserMapping(null, fromUserId, toUserId, loggedInUserId));
                } else {
                	UserMapping um = userMappingRepo.findByManagerIdAndCoachId(fromUserId, toUserId);
                	um.setActive(true);
                	um.setDeLinkedBy(null);
                	mapList.add(um);
                }
            }
        } else {
            throw new CommonException("Invalid role Mapping");
        }
        userMappingRepo.saveAll(mapList);
        return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(), "Mapped Successfully");

    }

    @Override
    public ApiReturn deLinkMapping(UserMapVo userMapVo) throws CommonException {
    	Long loggedInUserId = Long.parseLong(MDC.get("userId"));

        List<UserMapping> mapList = new ArrayList<>();
        Long fromUserId = userMapVo.getFromUserId();
        if (userMapVo.getFromRoleId() == (Constants.MANAGER) && userMapVo.getToRoleId() == (Constants.COACH)) {
            for (Long toUserId : userMapVo.getToUserId()) {
                if (userMappingRepo.existsByManagerIdAndCoachId(fromUserId, toUserId)) {
                	UserMapping um = userMappingRepo.findByManagerIdAndCoachId(fromUserId, toUserId);
                	um.setActive(false);
                	um.setDeLinkedBy(fromUserId);
                    mapList.add(um);
                }
            }
        } else if (userMapVo.getFromRoleId() == (Constants.MANAGER)
                && userMapVo.getToRoleId() == (Constants.EMPLOYEE)) {
            for (Long toUserId : userMapVo.getToUserId()) {
                if (userMappingRepo.existsByManagerIdAndEmployeeId(fromUserId, toUserId)) {
                	UserMapping um = userMappingRepo.findByManagerIdAndCoachId(fromUserId, toUserId);
                	um.setActive(false);
                	um.setDeLinkedBy(fromUserId);
                    mapList.add(um);
                }
            }
        } else if (userMapVo.getFromRoleId() == (Constants.COACH) && userMapVo.getToRoleId() == (Constants.EMPLOYEE)) {
            for (Long toUserId : userMapVo.getToUserId()) {
                if (userMappingRepo.existsByCoachIdAndEmployeeId(fromUserId, toUserId)) {
                    UserMapping um = userMappingRepo.findByManagerIdAndCoachId(fromUserId, toUserId);
                	um.setActive(false);
                	um.setDeLinkedBy(fromUserId);
                    mapList.add(um);
                }
            }
        } else {
            throw new CommonException("Invalid role Mapping");
        }
        userMappingRepo.saveAll(mapList);
        return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(), "Mapped Successfully");
    }

    @Override
    public ApiReturn getAllocatedEmployeesByCoachId(Long employeeTypeId, Long userId) throws CommonException {
        List<Long> employeeIds = null;
        if (employeeTypeId.equals(Long.valueOf(Constants.EMPLOYEE))) {
            Role role = roleRepo.findById(Long.valueOf(Constants.COACH)).orElseThrow(() -> new CommonException("Role Not Found"));
            if (userRoleRepo.existsByUserIdAndRoleId(userId, role)) {
                employeeIds = userMappingRepo.findEmployeeIdByCoachId(userId);
            }
        } else if (employeeTypeId.equals(Long.valueOf(Constants.COACH))) {
            Role role = roleRepo.findById(Long.valueOf(Constants.MANAGER)).orElseThrow(() -> new CommonException("Role Not Found"));
            if (userRoleRepo.existsByUserIdAndRoleId(userId, role)) {
                employeeIds = userMappingRepo.findCoachIdByManagerId(userId);
            }
        } else if (employeeTypeId.equals(Long.valueOf(Constants.MANAGER))) {
            Role role = roleRepo.findById(Long.valueOf(Constants.MANAGER)).orElseThrow(() -> new CommonException("Role Not Found"));
            if (userRoleRepo.existsByUserIdAndRoleId(userId, role)) {
                employeeIds = userMappingRepo.findEmployeeIdByManagerId(userId);
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
