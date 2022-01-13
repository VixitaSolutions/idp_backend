package com.oversoul.security.jwt.extractor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

/**
 * An implementation of {@link TokenExtractor} extracts token from
 * Authorization: Bearer scheme.
 */
@Component
public class JwtHeaderTokenExtractor implements TokenExtractor {

	public static String HEADER_PREFIX = "Bearer ";

	@Autowired
	private MessageSource messageSource;

	@Override
	public String extract(String header) {
		if (StringUtils.isBlank(header)) {
			throw new AuthenticationServiceException(messageSource.getMessage("authHeaderCantBeBlank", null, null));
		}

		if (header.length() < HEADER_PREFIX.length()) {
			throw new AuthenticationServiceException(messageSource.getMessage("invalidAuthHeaderSize", null, null));
		}

		return header.substring(HEADER_PREFIX.length(), header.length());
	}
}
