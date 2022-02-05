package com.oversoul.controller;

import com.oversoul.exception.CommonException;
import com.oversoul.service.UserRoleService;
import com.oversoul.vo.ApiReturn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/userRole/")
@SecurityRequirement(name = "Authorization")
public class UserRoleController {

    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @GetMapping("add")
    public ApiReturn addRole(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId) throws CommonException {

        return userRoleService.addRole(userId, roleId);

    }
}
