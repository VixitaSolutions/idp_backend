package com.oversoul.service;

import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.UserSignUpVo;

public interface LoginService {

	ApiReturn sendOtp(UserSignUpVo userSignUpVo) throws Exception;

	ApiReturn verifyOtp(UserSignUpVo userSignUpVo);

	ApiReturn sendTemporaryPassword(UserSignUpVo userSignUpVo);

	ApiReturn verifyTemporaryPassword(UserSignUpVo userSignUpVo);

	ApiReturn createPassword(UserSignUpVo userSignUpVo);

}
