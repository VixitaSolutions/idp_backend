package com.oversoul.security.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oversoul.security.service.HelperService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenFactory {

	private static Logger LOG = LoggerFactory.getLogger(JwtTokenFactory.class);

	private final JwtSettings settings;

	@Autowired
	private HelperService helperService;

	@Autowired
	public JwtTokenFactory(JwtSettings settings) {
		this.settings = settings;
	}

	/**
	 * Factory method for issuing new JWT Tokens.
	 * 
	 * @param username
	 * @param roles
	 * @return
	 */
	public AccessJwtToken createAccessJwtToken(UserContext userContext) {
		if (StringUtils.isBlank(userContext.getUserId()))
			throw new IllegalArgumentException("Cannot create JWT Token without userId");

		if (userContext.getAuthorities() == null || userContext.getAuthorities().isEmpty())
			throw new IllegalArgumentException("User doesn't have any privileges");

		Claims claims = Jwts.claims().setSubject(userContext.getUserId());
		claims.put("scopes", userContext.getAuthorities().stream().map(s -> "ROLE_".concat(s.toString()))
				.collect(Collectors.toList()));

		LocalDateTime currentTime = LocalDateTime.now();

		String signingKey = helperService.getJwtSigningKey();
		LOG.info("Signing in with :: " + signingKey);

		String token = Jwts.builder().setClaims(claims).setIssuer(settings.getTokenIssuer())
				.setHeaderParam("typ", "JWT")
				.setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
				.setExpiration(Date.from(currentTime.plusMinutes(settings.getTokenExpirationTime())
						.atZone(ZoneId.systemDefault()).toInstant()))
				.signWith(SignatureAlgorithm.HS512, signingKey).compact();
		return new AccessJwtToken(token, claims);
	}

	public JwtToken createRefreshToken(UserContext userContext) {
		if (StringUtils.isBlank(userContext.getUserId())) {
			throw new IllegalArgumentException("Cannot create JWT Token without userId");
		}

		LocalDateTime currentTime = LocalDateTime.now();

		Claims claims = Jwts.claims().setSubject(userContext.getUserId());
		claims.put("scopes", Arrays.asList(Scopes.REFRESH_TOKEN.authority()));
		claims.put("loginType", userContext.getLoginType());
		String token = Jwts.builder().setClaims(claims).setIssuer(settings.getTokenIssuer())
				.setId(UUID.randomUUID().toString())
				.setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
				.setExpiration(Date.from(currentTime.plusMinutes(settings.getRefreshTokenExpTime())
						.atZone(ZoneId.systemDefault()).toInstant()))
				.signWith(SignatureAlgorithm.HS512, helperService.getJwtSigningKey()).compact();
		return new AccessJwtToken(token, claims);
	}
}
