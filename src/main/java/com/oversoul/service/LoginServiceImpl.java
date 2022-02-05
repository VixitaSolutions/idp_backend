package com.oversoul.service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.oversoul.entity.User;
import com.oversoul.entity.UserOtpDetails;
import com.oversoul.repository.UserOtpDetailsRepository;
import com.oversoul.repository.UserRepository;
import com.oversoul.util.ApiConstants;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.UserSignUpVo;

@Service
public class LoginServiceImpl implements LoginService {

	private static final String USER_NOT_FOUND_OR_INACTIVE = "User Not Found or Inactive";

	private final UserRepository userRepo;

	private final UserOtpDetailsRepository userOtpDetailsRepo;

	public LoginServiceImpl(UserRepository userRepo, UserOtpDetailsRepository userOtpDetailsRepo) {
		this.userRepo = userRepo;
		this.userOtpDetailsRepo = userOtpDetailsRepo;
	}

	@Override
	public ApiReturn sendOtp(UserSignUpVo userSignUpVo) {

		Long userId = userRepo.findIdByEmail(userSignUpVo.getEmail());
		if (userId != null) {
			Integer otp = (int) (Math.floor(100000 + Math.random() * 900000));
			userOtpDetailsRepo.save(new UserOtpDetails(userId, otp.toString(), userSignUpVo.getOtpType()));
			return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(), "OTP Sent Successfully");
		}
		return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.FAILED.name(), USER_NOT_FOUND_OR_INACTIVE);

	}

	@Override
	public ApiReturn verifyOtp(UserSignUpVo userSignUpVo) {

		Long userId = userRepo.findIdByEmail(userSignUpVo.getEmail());
		if (userId != null) {
			Calendar currentTimeNow = Calendar.getInstance();
			currentTimeNow.add(Calendar.MINUTE, -5);
			boolean isVerify = userOtpDetailsRepo.existsByUserIdAndOtpTypeAndOtpAndUpdatedOnGreaterThanEqual(userId,
					userSignUpVo.getOtpType(), userSignUpVo.getOtp(), currentTimeNow.getTime());
			if (isVerify) {
				return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
						"OTP Verification Success");
			} else {
				return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.FAILED.name(),
						"OTP Verification Failed");
			}
		}
		return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.FAILED.name(), USER_NOT_FOUND_OR_INACTIVE);

	}

	@Override
	public ApiReturn sendTemporaryPassword(UserSignUpVo userSignUpVo) {

		User user = userRepo.findByEmailAndTenantId(userSignUpVo.getEmail(), UUID.fromString(MDC.get("tenantId")));
		if (user != null) {
			Integer otp = (int) (Math.floor(100000 + Math.random() * 900000));
			user.setPassword(otp.toString());
			userRepo.save(user);
			return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
					"Temporary Password Sent Successfully");
		}
		return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.FAILED.name(), USER_NOT_FOUND_OR_INACTIVE);

	}

	@Override
	public ApiReturn verifyTemporaryPassword(UserSignUpVo userSignUpVo) {
		User user = userRepo.findByEmail(userSignUpVo.getEmail());
		if (user != null && userSignUpVo.getOtp().equals(user.getPassword())) {
			return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
					"Temporary Password verified Successfully");
		}
		return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.FAILED.name(), "Invalid Password");

	}

	@Override
	public ApiReturn createPassword(UserSignUpVo userSignUpVo) {
		User user = userRepo.findByEmail(userSignUpVo.getEmail());
		if (user != null) {
			user.setPassword(userSignUpVo.getOtp());
			user.setSignUpDone(true);
			user.setSignUpDoneOn(new Date());
			userRepo.save(user);
			return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
					"Password updated Successfully");
		}
		return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.FAILED.name(), USER_NOT_FOUND_OR_INACTIVE);
	}

}
