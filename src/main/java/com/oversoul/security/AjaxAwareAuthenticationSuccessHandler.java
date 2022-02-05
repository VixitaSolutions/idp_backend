package com.oversoul.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oversoul.common.AppProperties;
import com.oversoul.entity.User;
import com.oversoul.entity.UserRole;
import com.oversoul.repository.UserRepository;
import com.oversoul.repository.UserRoleRepository;
import com.oversoul.security.model.JwtToken;
import com.oversoul.security.model.JwtTokenFactory;
import com.oversoul.security.model.Tokens;
import com.oversoul.security.model.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * AjaxAwareAuthenticationSuccessHandler
 */
@Component
public class AjaxAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final Logger log = LoggerFactory.getLogger(AjaxAwareAuthenticationSuccessHandler.class);

    private final ObjectMapper mapper;
    private final JwtTokenFactory tokenFactory;
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    AppProperties appProperties;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public AjaxAwareAuthenticationSuccessHandler(final ObjectMapper mapper, final JwtTokenFactory tokenFactory) {
        this.mapper = mapper;
        this.tokenFactory = tokenFactory;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        UserContext userContext = (UserContext) authentication.getPrincipal();

        LocalDateTime currentTime = LocalDateTime.now();

        Optional<User> user = userRepository.findById(Long.parseLong(userContext.getUserId()));

        if (user.isPresent()) {
            // if (userService.getUserOtpLoginIsExpired(user.getId())) {
            if (false) {
                log.info("Otp Limit Exceeded while login");
                Map<String, Object> tokenMaps = new HashMap<String, Object>();
                tokenMaps.put("status", HttpStatus.BAD_REQUEST.value());
                // tokenMaps.put("message", appProperties.getOtpLimitExceededMessage());
                // tokenMaps.put("messageCode", appProperties.getOtpLimitExceededCode());
                tokenMaps.put("data", null);
                tokenMaps.put("timestamp", System.currentTimeMillis() / 1000L);
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                mapper.writeValue(response.getWriter(), tokenMaps);

            } else {
                Date createdDate = Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());
                JwtToken accessToken = tokenFactory.createAccessJwtToken(userContext);
                JwtToken refreshToken = tokenFactory.createRefreshToken(userContext);

                Map<String, Object> tokenMap = new HashMap<String, Object>();
                tokenMap.put("status", HttpStatus.OK.value());
                tokenMap.put("message", "Login successful");
                Tokens tokens = new Tokens();
                tokens.setRefreshToken(refreshToken.getToken());
                tokens.setToken(accessToken.getToken());

                if (!user.isPresent()) {

                    Map<String, Object> tokenMaps = new HashMap<String, Object>();
                    tokenMaps.put("status", HttpStatus.BAD_REQUEST.value());
                    tokenMaps.put("message", "No active roles Found, please contact Administrator");
                    tokenMaps.put("data", null);
                    tokenMaps.put("timestamp", System.currentTimeMillis() / 1000L);
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    mapper.writeValue(response.getWriter(), tokenMaps);
                } else {
                    User user2 = user.get();
                    user2.setLastLoginTime(createdDate.getTime());
                    userRepository.save(user2);
                    UserRole userRole = userRoleRepository.findByUserId(user.get().getId());
                    tokens.setRoleIds(userRole.getRoleId().getId());
                    log.info("User with Id : " + userContext.getUserId() + " logged in! and token :: "
                            + accessToken.getToken() + "  ::::::::: Refresh Token ::::: " + refreshToken.getToken());
                    tokenMap.put("data", tokens);
                    tokenMap.put("timestamp", System.currentTimeMillis() / 1000L);

                    response.setStatus(HttpStatus.OK.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    mapper.writeValue(response.getWriter(), tokenMap);

                    clearAuthenticationAttributes(request);
                }

            }
        }

    }

    /**
     * Removes temporary authentication-related data which may have been stored in
     * the session during the authentication process.
     */
    protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
