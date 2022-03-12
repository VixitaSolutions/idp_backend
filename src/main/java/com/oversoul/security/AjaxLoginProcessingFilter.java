package com.oversoul.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oversoul.security.exceptions.AuthMethodNotSupportedException;
import com.oversoul.security.model.LoginRequest;
import com.oversoul.security.model.LoginType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AjaxLoginProcessingFilter
 */
public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private static final Logger log = LoggerFactory.getLogger(AjaxLoginProcessingFilter.class);

    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;

    private final ObjectMapper objectMapper;

    @Autowired
    private MessageSource messageSource;

    public AjaxLoginProcessingFilter(String defaultProcessUrl, AuthenticationSuccessHandler successHandler,
                                     AuthenticationFailureHandler failureHandler, ObjectMapper mapper) {
        super(defaultProcessUrl);
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.objectMapper = mapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (!HttpMethod.POST.name().equals(request.getMethod()) /*|| !WebUtil.isAjax(request)*/) {
            if (log.isDebugEnabled()) {
                log.debug("Authentication method not supported. Request method: {} ", request.getMethod());
            }
            throw new AuthMethodNotSupportedException(messageSource.getMessage("httpMethodNotSupported", null, null));
        }

        UsernamePasswordAuthentication token = null;
        LoginRequest loginRequest = null;

        try {
            loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);
        } catch (Exception e) {
            throw new AuthenticationServiceException(messageSource.getMessage("invalidInputs", null, null));
        }

        // Handle respective exceptional cases and process basing on loginType
        if (loginRequest != null) {
            if (loginRequest.getLoginType() == null) {
                throw new AuthenticationServiceException(
                        messageSource.getMessage("loginTypeKeyNotProvided", null, null));
            } else if (loginRequest.getLoginType() == LoginType.byUserName) {
                if (StringUtils.isBlank(loginRequest.getUsername())
                        || StringUtils.isBlank(loginRequest.getPassword())) {
                    throw new AuthenticationServiceException(
                            messageSource.getMessage("userOrPwdKeyNotProvided", null, null));

                }
                token = new UsernamePasswordAuthentication(loginRequest.getUsername(), loginRequest.getPassword(),
                        loginRequest.getLoginType(), loginRequest.getTenant());
            } else {
                throw new AuthenticationServiceException(messageSource.getMessage("loginTypeNotMatched", null, null));
            }
        }
        return this.getAuthenticationManager().authenticate(token);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

}
