package com.oversoul.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.oversoul.common.EncryptionDecryptionAES;
import com.oversoul.entity.User;
import com.oversoul.entity.UserRole;
import com.oversoul.repository.UserRepository;
import com.oversoul.repository.UserRoleRepository;
import com.oversoul.security.exceptions.ConflictException;
import com.oversoul.security.exceptions.InvalidOAuthAccess;
import com.oversoul.security.model.LoginType;
import com.oversoul.security.model.UserContext;

@Component
public class AjaxAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	EncryptionDecryptionAES aes;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private MessageSource messageSource;

	@Value("${MASTER_PWD}")
	public String MASTER_PWD;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try {
			// Assert.notNull(authentication,
			// messageSource.getMessage("noAuthenticationDataProvided", null,
			// LocaleContextHolder.getLocale()));

			UsernamePasswordAuthentication userAuthentication = null;
			UserContext userContext = null;
			String username = null;

			if (authentication instanceof UsernamePasswordAuthentication) {
				userAuthentication = (UsernamePasswordAuthentication) authentication;
				if (userAuthentication.getPrincipal() != null) {
					username = (String) userAuthentication.getPrincipal().toString().toLowerCase();
				}
				String password = (String) userAuthentication.getCredentials();
				String authId = (String) userAuthentication.getAuthId();
				LoginType loginType = (LoginType) userAuthentication.getLoginType();

				UserRole userRoles = null;
				String loggedUser = null;

				if (loginType == null) {
					throw new BadCredentialsException("Login Type required");
				}
				System.out.println("auth id ==== " + authId);
				// User user =
				// userRepository.findByEmail(EncryptionDecryptionAES.encrypt(username.trim().toLowerCase()));

				User user = userRepository.findByEmail(username.trim().toLowerCase());
				/*
				 * if login type is by user name means it will load the login details validate
				 * the password for given username
				 */

				if (user == null) {
					throw new BadCredentialsException(
							messageSource.getMessage("emailOrPwdInvalid", null, LocaleContextHolder.getLocale()));
				}

				if (LoginType.byUserName == loginType) {

					if (username == null || password == null) {
						throw new BadCredentialsException(
								messageSource.getMessage("emailOrPwdInvalid", null, LocaleContextHolder.getLocale()));
					}

					try {
						// String decrypt = EncryptionDecryptionAES.decrypt(user.getPassword());
						System.out.println(
								user.getPassword() + "::::::::::::: encoded pwd =========" + user.getPassword());

						if (password.equals(MASTER_PWD)) {
							// allow with master pwd
						} else if (!(user.getPassword().equals(password))) {
							throw new BadCredentialsException(
									messageSource.getMessage("authFailed", null, LocaleContextHolder.getLocale()));
						}
					} catch (Exception e) {

						throw new BadCredentialsException(
								messageSource.getMessage("authFailed", null, LocaleContextHolder.getLocale()));
					}

				}

				loggedUser = user.getEmail();
				// userDetails = user;
				/* fetch the rules based on userId */
				userRoles = userRoleRepository.findByUserId(user);
				List<String> roleString = new ArrayList<>();
				if (userRoles == null) {
					throw new InsufficientAuthenticationException("Insufficient role permissions");
				}
				roleString = Arrays.asList(userRoles.getRoleId().getName());

				List<GrantedAuthority> authorities = roleString.stream()
						.map(authority -> new SimpleGrantedAuthority(authority)).collect(Collectors.toList());

				userContext = UserContext.createContext(loggedUser, user.getId().toString(), loginType, authorities);

				// send Signin OTP to user
				/*
				 * UserSignUpOtp signUpOtp = new UserSignUpOtp(); UserSignUpOtp userOtp =
				 * userSignUpOtpRepo.findByUserId(userDetails.getId());
				 * 
				 * if (userOtp == null) { signUpOtp.setUserId(userDetails.getId());
				 * signUpOtp.setType(EmailStat.SIGNUP_INVITE); Integer otp = (int)
				 * (Math.floor(100000 + Math.random() * 900000));
				 * signUpOtp.setCode(otp.toString()); signUpOtp =
				 * userSignUpOtpRepo.save(signUpOtp); } else { Integer otp = (int)
				 * (Math.floor(100000 + Math.random() * 900000));
				 * userOtp.setCode(otp.toString()); signUpOtp = userSignUpOtpRepo.save(userOtp);
				 * }
				 * 
				 * String emailContent = adminSignupservice.getSignUpOtpEmailContent(signUpOtp);
				 * LOG.info(" client email ======" + userDetails.getEmail());
				 * sesMailService.sendMail(userDetails.getEmail(), Constants.LOGIN_OTP,
				 * emailContent);
				 */

				/*
				 * if (LoginType.bySSO == loginType) { User user = new User();
				 * user.setSignUpViaSSO(true); userRepository.save(user); }
				 */

				return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
			}
			throw new InsufficientAuthenticationException(
					messageSource.getMessage("exceptionInLoginInputParsing", null, LocaleContextHolder.getLocale()));
		} catch (Exception e) {
			if (e instanceof UsernameNotFoundException) {
				throw new UsernameNotFoundException(e.getMessage());
			} else if (e instanceof BadCredentialsException) {
				throw new BadCredentialsException(e.getMessage());
			} else if (e instanceof InvalidOAuthAccess) {
				throw new InvalidOAuthAccess(e.getMessage());
			} else if (e instanceof ConflictException) {
				throw new ConflictException(e.getMessage());
			} else {
				e.printStackTrace();
				throw new InsufficientAuthenticationException(e.getMessage());
			}
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

}
