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
				String tenant = (String) userAuthentication.getTenant();
				userAuthentication.getTenant();

				UserRole userRoles = null;
				String loggedUser = null;

				if (loginType == null) {
					throw new BadCredentialsException("Login Type required");
				}
				if (username==null){
					throw new BadCredentialsException("UserName Type required");
				}
				if (tenant == null) {
					throw new BadCredentialsException("Tenant required");
				}
				System.out.println("auth id ==== " + authId);
				// User user =
				// userRepository.findByEmail(EncryptionDecryptionAES.encrypt(username.trim().toLowerCase()));

//				User user = userRepository.findByEmail(username.trim().toLowerCase());
				User user = userRepository.findIdByEmailAndTenant(username.trim().toLowerCase(), tenant.trim().toLowerCase());
				/*
				 * if login type is by username means it will load the login details validate
				 * the password for given username
				 */

				if (user == null) {
					throw new BadCredentialsException(
							messageSource.getMessage("emailOrPwdInvalid", null, LocaleContextHolder.getLocale()));
				}

				if (LoginType.byUserName == loginType) {

					if (password == null) {
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
				userRoles = userRoleRepository.findByUserId(user.getId());
				List<String> roleString = new ArrayList<>();
				if (userRoles == null) {
					throw new InsufficientAuthenticationException("Insufficient role permissions");
				}
				roleString = Arrays.asList(userRoles.getRoleId().getName());

				List<GrantedAuthority> authorities = roleString.stream()
						.map(SimpleGrantedAuthority::new).collect(Collectors.toList());

				userContext = UserContext.createContext(loggedUser, user.getId().toString(), loginType, authorities, user.getTenantId());

				return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
			}
			throw new InsufficientAuthenticationException(
					messageSource.getMessage("exceptionInLoginInputParsing", null, LocaleContextHolder.getLocale()));
		} catch (Exception e) {
			if (e instanceof UsernameNotFoundException) throw new UsernameNotFoundException(e.getMessage());
			else if (e instanceof BadCredentialsException) {
				throw new BadCredentialsException(e.getMessage());
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
