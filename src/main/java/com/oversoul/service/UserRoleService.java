package com.oversoul.service;

import com.oversoul.exception.CommonException;
import com.oversoul.vo.ApiReturn;

public interface UserRoleService {
    ApiReturn addRole(Long userId, Long roleId) throws CommonException;

    ApiReturn removeRole(Long userId, Long roleId) throws CommonException;
}
