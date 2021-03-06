package com.oversoul.security;

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
import com.oversoul.security.endpoint.RestAuthenticationEntryPoint;
import com.oversoul.security.jwt.extractor.TokenExtractor;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	public static final String JWT_TOKEN_HEADER_PARAM = "Authorization";

	public static final String FORM_BASED_LOGIN_ENTRY_POINT = "/api/auth/login";

	public static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/api/v1/**";

	public static final String TOKEN_REFRESH_ENTRY_POINT = "/api/v1/login/**";

	public static final String TENANTS_ENDPOINT = "/api/v1/tenant/get";

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

	protected AjaxLoginProcessingFilter buildAjaxLoginProcessingFilter() {
		AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter(FORM_BASED_LOGIN_ENTRY_POINT, successHandler,
				failureHandler, objectMapper);
		filter.setAuthenticationManager(this.authenticationManager);
		return filter;
	}

	protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter() {
		List<String> pathsToSkip = Arrays.asList(TOKEN_REFRESH_ENTRY_POINT,TENANTS_ENDPOINT);
		SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, TOKEN_BASED_AUTH_ENTRY_POINT);
		JwtTokenAuthenticationProcessingFilter filter = new JwtTokenAuthenticationProcessingFilter(failureHandler,
				tokenExtractor, matcher);
		filter.setAuthenticationManager(this.authenticationManager);
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
				.and().authorizeRequests().antMatchers(TENANTS_ENDPOINT).permitAll()
				.antMatchers(TOKEN_REFRESH_ENTRY_POINT).permitAll().and().authorizeRequests()
				.antMatchers(TOKEN_BASED_AUTH_ENTRY_POINT).authenticated() // Protected
				// API
				// End-points
				.and().addFilterBefore(buildAjaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)

				.addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(),
						UsernamePasswordAuthenticationFilter.class);
	}

}
