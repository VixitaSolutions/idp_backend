package com.oversoul.security.endpoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.oversoul.entity.User;
import com.oversoul.entity.UserRole;
import com.oversoul.repository.UserRepository;
import com.oversoul.repository.UserRoleRepository;
import com.oversoul.security.WebSecurityConfig;
import com.oversoul.security.exceptions.JwtExpiredTokenException;
import com.oversoul.security.jwt.extractor.TokenExtractor;
import com.oversoul.security.model.JwtSettings;
import com.oversoul.security.model.JwtTokenFactory;
import com.oversoul.security.model.RawAccessJwtToken;
import com.oversoul.security.model.RefreshToken;
import com.oversoul.security.model.TokenVerifier;
import com.oversoul.security.model.UserContext;
import com.oversoul.vo.ApiReturn;
import com.oversoul.vo.ApiReturnWithResult;

/**
 * RefreshTokenEndpoint
 */
@RestController
public class RefreshTokenEndpoint {

	@Autowired
	private JwtTokenFactory tokenFactory;

	// @Autowired
	private TokenVerifier tokenVerifier;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	@Qualifier("jwtHeaderTokenExtractor")
	private TokenExtractor tokenExtractor;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private JwtSettings jwtSettings;

	@Autowired
	private UserRoleRepository userRoleRepo;

	@RequestMapping(value = "/api/auth/refreshToken", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody ApiReturn refreshToken(HttpServletRequest request, HttpServletResponse response) {

		try {
			String tokenPayload = tokenExtractor.extract(request.getHeader(WebSecurityConfig.JWT_TOKEN_HEADER_PARAM));

			RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenPayload);
			Optional<RefreshToken> refreshToken = RefreshToken.create(rawToken, jwtSettings.getTokenSigningKey());

			if (!refreshToken.isPresent()) {
				return new ApiReturn(HttpStatus.UNAUTHORIZED.value(), "Invalid JWT. Please verify", null);
			}
			String jti = refreshToken.get().getJti();
			if (!tokenVerifier.verify(jti)) {
				return new ApiReturn(HttpStatus.UNAUTHORIZED.value(), "Invalid JWT. Please verify", null);
			}

			String subject = refreshToken.get().getSubject();
			long issuedDate = refreshToken.get().getClaims().getBody().getIssuedAt().getTime();
			UserContext userContext = null;
			String loggedUser = null;

			long lastResetTokenDate = 0;
			Optional<User> user = userRepository.findById(Long.parseLong(subject));
			if (!user.isPresent())
				throw new UsernameNotFoundException(
						messageSource.getMessage("userNotFound", null, request.getLocale()));
			lastResetTokenDate = user.get().getLastLoginTime();
			lastResetTokenDate /= 1000;
			issuedDate /= 1000;
			if (issuedDate < lastResetTokenDate) {
				throw new JwtExpiredTokenException(messageSource.getMessage("invalidToken", null, request.getLocale()));
			}

			loggedUser = user.get().getEmail();
			UserRole userRoles = userRoleRepo.findByUserId(user.get().getId());
			List<String> roleString = new ArrayList<>();
			if (userRoles == null) {
				throw new InsufficientAuthenticationException("Insufficient role permissions");
			}
			roleString = Arrays.asList(userRoles.getRoleId().getName());

			List<GrantedAuthority> authorities = roleString.stream()
					.map(authority -> new SimpleGrantedAuthority(authority)).collect(Collectors.toList());

			userContext = UserContext.create(loggedUser, user.get().getId().toString(), null, authorities, null);

			return new ApiReturnWithResult(HttpStatus.OK.value(),
					messageSource.getMessage("tokenProvided", null, request.getLocale()),
					tokenFactory.createAccessJwtToken(userContext));

		} catch (Exception e) {
			return new ApiReturn(HttpStatus.EXPECTATION_FAILED.value(),
					messageSource.getMessage("invalidTokenProvided", null, request.getLocale()), null);

		}
	}

}
