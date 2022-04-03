package com.oversoul.controller;

import com.oversoul.exception.CommonException;
import com.oversoul.service.UserService;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.UserListReq;
import com.oversoul.vo.UserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/")
@SecurityRequirement(name = "Authorization")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        super();
        this.userService = userService;
    }

    @Operation(summary = "This is to fetch user profile by token")
    @GetMapping("profile")
    public ApiReturn getProfile() {
        return userService.getProfile();
    }

    @PostMapping("create")
    public ApiReturn createUser(@RequestBody UserRequest userRequest) throws CommonException {
        return userService.createUser(userRequest);
    }

    //we need to fetch by tenantId, role Type
    @Operation(summary = "This is to fetch users by client Id")
    @PostMapping("userList")
    public ApiReturn getEmployeesByClient(@RequestBody UserListReq client) throws CommonException {
        return userService.getUserList(client);
    }

    @GetMapping("update/status/{userId}")
    public ApiReturn changeStatus(@RequestParam("status") boolean status, @PathVariable("userId") Long userId) throws CommonException {
        return userService.changeStatus(status, userId);
    }

    @GetMapping("assignedUser/{userId}")
    public ApiReturn getAssignedUserDetails(@PathVariable("userId") Long userId) throws CommonException {
		return userService.getAssignedUserDetails(userId);
    }

}
