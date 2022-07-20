package com.oversoul.service;

import com.oversoul.entity.User;
import com.oversoul.entity.UserOtpDetails;
import com.oversoul.repository.UserOtpDetailsRepository;
import com.oversoul.repository.UserRepository;
import com.oversoul.util.ApiConstants;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.UserSignUpVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.slf4j.MDC;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    private static final String USER_NOT_FOUND_OR_INACTIVE = "User Not Found or Inactive";

    private final UserRepository userRepo;

    private final UserOtpDetailsRepository userOtpDetailsRepo;

    private final AmazonSESService amazonSESService;

    public LoginServiceImpl(UserRepository userRepo, UserOtpDetailsRepository userOtpDetailsRepo, AmazonSESService amazonSESService) {
        this.userRepo = userRepo;
        this.userOtpDetailsRepo = userOtpDetailsRepo;
        this.amazonSESService = amazonSESService;
    }

    @Override
    public ApiReturn sendOtp(UserSignUpVo userSignUpVo) throws Exception {

        Long userId = userRepo.findIdByEmail(userSignUpVo.getEmail());
        if (userId != null) {
            Integer otp = (int) (Math.floor(100000 + Math.random() * 900000));
            amazonSESService.sendMail(userSignUpVo.getEmail(),formEmailContent(otp));
            userOtpDetailsRepo.save(new UserOtpDetails(userId, otp.toString(), userSignUpVo.getOtpType()));
            return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(), "OTP Sent Successfully");
        }
        return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.FAILED.name(), USER_NOT_FOUND_OR_INACTIVE);

    }

    private String formEmailContent(Integer otp) {
        try {
            ClassPathResource classPathResource = new ClassPathResource("/email_content/sign_up_otp.html");
            InputStream inputStream = classPathResource.getInputStream();
            String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            inputStream.close();

            // Set button click URL
            content = content.replace("SignUpOtp", otp.toString());

            return content;
        } catch (Exception e) {
            log.error("Error reading verification email content: ", e);
        }
        return "";
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
    public ApiReturn sendTemporaryPassword(UserSignUpVo userSignUpVo) throws Exception {

        User user = userRepo.findByEmailAndTenantId(userSignUpVo.getEmail(), UUID.fromString(MDC.get("tenantId")));
        if (user != null) {
            Integer otp = (int) (Math.floor(100000 + Math.random() * 900000));
            amazonSESService.sendMail(userSignUpVo.getEmail(),formEmailContent(otp));
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
        	if (!userSignUpVo.getCurrentPassword().isEmpty()) {
        		if (!userRepo.findByEmailAndPassword(userSignUpVo.getEmail(), userSignUpVo.getCurrentPassword()).isPresent()) {
        			return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.FAILED.name(),
                            "Current Password is invalid");
        		}
    			user.setPassword(userSignUpVo.getOtp());
                user.setSignUpDone(true);
                user.setSignUpDoneOn(new Date());
                userRepo.save(user);
                return new ApiReturn(HttpStatus.OK.value(), ApiConstants.Status.SUCCESS.name(),
                        "Password updated Successfully");
        	}
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
