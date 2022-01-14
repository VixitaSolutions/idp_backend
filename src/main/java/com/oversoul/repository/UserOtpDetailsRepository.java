package com.oversoul.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oversoul.entity.UserOtpDetails;
import com.oversoul.enums.OtpType;

public interface UserOtpDetailsRepository extends JpaRepository<UserOtpDetails, Long> {

	boolean existsByUserIdAndOtpTypeAndOtpAndUpdatedOnGreaterThanEqual(Long userId, OtpType otpType, String otp,
			Date time);

}
