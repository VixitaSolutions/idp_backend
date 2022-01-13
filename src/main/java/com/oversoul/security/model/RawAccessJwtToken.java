package com.oversoul.security.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;

import com.oversoul.common.AppProperties;
import com.oversoul.security.exceptions.JwtExpiredTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class RawAccessJwtToken implements JwtToken {
	private static Logger logger = LoggerFactory.getLogger(RawAccessJwtToken.class);

	private String token;
	
	@Autowired
	AppProperties appProperties;

	public RawAccessJwtToken(String token) {
		this.token = token;
	}

	/**
	 * Parses and validates JWT Token signature.
	 * 
	 * @throws BadCredentialsException
	 * @throws JwtExpiredTokenException
	 * 
	 */
	public Jws<Claims> parseClaims(String signingKey) {
		try {
			return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(this.token);
		} catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
			logger.error("Invalid JWT Token", ex);
			throw new BadCredentialsException("session expired", ex);
		} catch (ExpiredJwtException expiredEx) {
			logger.info("JWT Token is expired", expiredEx);
			throw new JwtExpiredTokenException(this, "session expired", expiredEx);
		}
	}

	@Override
	public String getToken() {
		return token;
	}
}
