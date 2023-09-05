package com.oversoul.vo;

import javax.annotation.Nonnull;
import javax.annotation.meta.When;

import com.oversoul.enums.OtpType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class UserSignUpVo {

	@Nonnull(when = When.ALWAYS)
	private String email;

	private String otp;

	@Nonnull(when = When.ALWAYS)
	private OtpType otpType;

	@Nonnull(when = When.ALWAYS)
	private String currentPassword;

}
