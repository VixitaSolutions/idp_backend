package com.oversoul.service;

import java.util.UUID;

import com.oversoul.exception.CommonException;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.UserRequest;

public interface UserService {

	ApiReturn getProfile();

	ApiReturn createUser(UserRequest userRequest) throws CommonException;

	ApiReturn getUserList(UUID clientId);

	ApiReturn changeStatus(boolean status, Long userId) throws CommonException;
}
