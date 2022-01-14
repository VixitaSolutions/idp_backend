package com.oversoul.security.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oversoul.security.AjaxAuthenticationProvider;
import com.oversoul.security.AjaxLoginProcessingFilter;
import com.oversoul.security.JwtAuthenticationProvider;
import com.oversoul.security.JwtTokenAuthenticationProcessingFilter;
import com.oversoul.security.SkipPathRequestMatcher;
import com.oversoul.security.endpoint.RestAuthenticationEntryPoint;
import com.oversoul.security.jwt.extractor.TokenExtractor;
import com.oversoul.security.service.HelperService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	public static final String JWT_TOKEN_HEADER_PARAM = "Authorization";

	public static final String FORM_BASED_LOGIN_ENTRY_POINT = "/api/auth/login";

	public static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/api/**";

	public static final String TOKEN_REFRESH_ENTRY_POINT = "/api/v1/login/**";

	public static final String REFRESH_ENTRY_POINT_V2 = "/api/auth/v2/token";

	@Autowired
	private RestAuthenticationEntryPoint authenticationEntryPoint;

	@Autowired
	private AuthenticationSuccessHandler successHandler;

	@Autowired
	private AuthenticationFailureHandler failureHandler;

	@Autowired
	private AjaxAuthenticationProvider ajaxAuthenticationProvider;

	@Autowired
	private JwtAuthenticationProvider jwtAuthenticationProvider;

	@Autowired
	private TokenExtractor tokenExtractor;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private HelperService helperService;

	protected AjaxLoginProcessingFilter buildAjaxLoginProcessingFilter() throws Exception {
		AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter(FORM_BASED_LOGIN_ENTRY_POINT, successHandler,
				failureHandler, objectMapper);
		filter.setAuthenticationManager(this.authenticationManager);
		filter.setHelperService(helperService);
		return filter;
	}

	protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter() throws Exception {
		List<String> pathsToSkip = Arrays.asList(TOKEN_REFRESH_ENTRY_POINT);
		SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, TOKEN_BASED_AUTH_ENTRY_POINT);
		JwtTokenAuthenticationProcessingFilter filter = new JwtTokenAuthenticationProcessingFilter(failureHandler,
				tokenExtractor, matcher);
		filter.setAuthenticationManager(this.authenticationManager);
		filter.setHelperService(helperService);
		return filter;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(ajaxAuthenticationProvider);
		auth.authenticationProvider(jwtAuthenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable() // We don't need CSRF for JWT based authentication
				.exceptionHandling().authenticationEntryPoint(this.authenticationEntryPoint)

				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

				.and().authorizeRequests().antMatchers(FORM_BASED_LOGIN_ENTRY_POINT).permitAll()// Login
				.antMatchers(TOKEN_REFRESH_ENTRY_POINT).permitAll() // Token
																	// refresh
																	// end-point
				.antMatchers(REFRESH_ENTRY_POINT_V2).permitAll()
				// H2
				// Console
				// Dash-board
				// -
				// only
				// for
				// testing
				.and().authorizeRequests().antMatchers(TOKEN_BASED_AUTH_ENTRY_POINT).authenticated() // Protected
																										// API
																										// End-points
				.and().addFilterBefore(buildAjaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)

				.addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(),
						UsernamePasswordAuthenticationFilter.class);
	}

}
