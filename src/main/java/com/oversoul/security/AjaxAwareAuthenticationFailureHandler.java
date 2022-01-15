package com.oversoul.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oversoul.security.exceptions.AuthMethodNotSupportedException;
import com.oversoul.security.exceptions.JwtExpiredTokenException;
import com.oversoul.vo.BaseResponse;

@Component
public class AjaxAwareAuthenticationFailureHandler implements AuthenticationFailureHandler {
	private final ObjectMapper mapper;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	public AjaxAwareAuthenticationFailureHandler(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException e) throws IOException, ServletException {

		BaseResponse baseResponse = new BaseResponse();

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		if (e instanceof BadCredentialsException) {
			baseResponse.setMessage(e.getMessage() != null ? e.getMessage()
					: messageSource.getMessage("invaliUserNameOrPwd", null, null));
			baseResponse.setStatus(HttpStatus.PRECONDITION_FAILED.value());

		} else if (e instanceof JwtExpiredTokenException) {
			baseResponse.setMessage(messageSource.getMessage("loginAgain", null, null));
			baseResponse.setStatus(HttpStatus.FORBIDDEN.value());
		} else if (e instanceof AuthMethodNotSupportedException) {
			baseResponse.setMessage(e.getMessage());
			baseResponse.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
		} else if (e instanceof UsernameNotFoundException && e.getMessage() != null) {
			baseResponse.setMessage(e.getMessage());
			baseResponse.setStatus(HttpStatus.PRECONDITION_FAILED.value());
		} else {
			baseResponse.setMessage(e.getMessage() != null ? e.getMessage()
					: messageSource.getMessage("authenticationFailed", null, null));
			baseResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

		mapper.writeValue(response.getWriter(), baseResponse);
	}
}
