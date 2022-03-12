package com.oversoul.controller;

import com.oversoul.exception.CommonException;
import com.oversoul.service.UserMappingService;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.UserMapVo;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usermap")
@SecurityRequirement(name = "Authorization")
public class UserMappingController {

    private final UserMappingService userMappingService;

    public UserMappingController(UserMappingService userMappingService) {
        this.userMappingService = userMappingService;
    }

    @PutMapping("link")
    public ApiReturn userMapping(@RequestBody UserMapVo userMapVo) throws CommonException {
        return userMappingService.userMapping(userMapVo);
    }

    @PutMapping("deLink")
    public ApiReturn deLinkMapping(@RequestBody UserMapVo userMapVo) throws CommonException {
        return userMappingService.deLinkMapping(userMapVo);
    }

    @GetMapping("allocated/{employeeTypeId}")
    public ApiReturn getAllocatedEmployees(@PathVariable Long employeeTypeId, @RequestParam("userId") Long coachId)
            throws CommonException {
        return userMappingService.getAllocatedEmployeesByCoachId(employeeTypeId, coachId);
    }

    @GetMapping("unAllocated")
    public ApiReturn getUnAllocatedEmployees(@RequestHeader("tenant-id")UUID tenantId)
            throws CommonException {
        return userMappingService.getUnAllocatedEmployees(tenantId);
    }
}
