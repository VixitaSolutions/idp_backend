package com.oversoul.security;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oversoul.common.WebUtil;
import com.oversoul.security.config.MultiReadHttpServletRequest;

@Component
@WebFilter("*")
@Order(Integer.MIN_VALUE)
public class SignatureFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(SignatureFilter.class);

	private static final String[] exemptedURLs = { "/health-check", "/api/auth/login" };

	@Value("${API.SIGNATURE.SECRET.KEY}")
	public String signatureSecretKey;

	public class Signature {

		private String signature;

		private String nonce;

		public String getNonce() {

			return nonce;
		}

		public void setNonce(String nonce) {
			this.nonce = nonce;
		}

		public String getSignature() {
			return signature;
		}

		public void setSignature(String signature) {
			this.signature = signature;
		}

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// empty
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		try {
			MultiReadHttpServletRequest request = new MultiReadHttpServletRequest((HttpServletRequest) req);

			MDC.put("uri", request.getRequestURI());
			MDC.clear();
			LOGGER.info("request data {}", request);
			if (!request.getMethod().equalsIgnoreCase("OPTIONS")) {
				// boolean validSignature = isValidSignature(request);
				if (true) {
					acceptRequest(request, resp, chain);
				} else {
					reject(request, resp);
				}
			} else {
				acceptRequest(request, resp, chain);
			}
		} catch (Exception e) {
			LOGGER.error("Exception in Signature filter", e);
		} finally {
		}

	}

	/*
	 * 
	 * for internal api's when calling other services generate signature using
	 * authorization token , nonce v4, secret key , strigified body - MD5fied
	 */

	public Signature generateSignature(HttpServletRequest request, Map<String, Object> requestObj)
			throws IOException, NoSuchAlgorithmException {

		Signature sign = new Signature();
		String nonce = null;
		// generation of nonce using uuid v4
		if (nonce == null) {
			String uuid = UUID.randomUUID().toString();
			System.out.println(uuid);
			nonce = uuid.toString();
		}
		// jwt token from header
		String authorization = WebUtil.getAuthorization(request);

		String apiData = "";
		if (authorization != null) {
			apiData += authorization;
		}
		if (nonce != null) {
			apiData += nonce;
		}
		// api body --- including internal server api's request body
		String requestBody = requestObj.toString();

		ObjectMapper mapper = new ObjectMapper();
		requestBody = mapper.writeValueAsString(requestObj);

		System.out.println("mapper === " + requestBody);
		if (!requestObj.isEmpty()) {
			apiData = apiData + requestBody;
		}
		if (!apiData.contains("password")) {
			LOGGER.info("Api Data: {}", apiData);
		}
		// MD5
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(apiData.getBytes());

		byte byteData[] = md.digest();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		// return signature
		String encodedSignature = sb.toString();
		LOGGER.info("Signature: {}", encodedSignature);
		// return nonce
		sign.setNonce(nonce);
		sign.setSignature(encodedSignature);
		return sign;
	}

	private void reject(HttpServletRequest req, ServletResponse resp) throws IOException {
		HttpServletResponse response = (HttpServletResponse) resp;
		LOGGER.error("Signature is invalid for URL {} and IP: {}", req.getRequestURI(), req.getRemoteAddr());
		// don't continue the chain
		response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "Signature is invalid");
	}

	private boolean isValidSignature(HttpServletRequest request)
			throws IOException, NoSuchAlgorithmException, ServletException {

		if (Arrays.asList(exemptedURLs).contains(request.getRequestURI())) {
			return true;
		}

		String signature = WebUtil.getSignature(request);
		String nonce = WebUtil.getNonce(request);
		String authorization = WebUtil.getAuthorization(request);

		String apiData = "";
		if (authorization != null) {
			apiData += authorization;
		}
		if (nonce != null) {
			apiData += nonce;
		}

		apiData += getSignatureSecretKey(request);

		LOGGER.info("CONTENT TYPE==== {}", request.getContentType());
		if (request.getContentType() == null || !request.getContentType().contains("multipart/form-data"))
			apiData = apiData + request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

		apiData = apiData + request.getRequestURI();

		if (request.getQueryString() != null)
			apiData = apiData + "?" + request.getQueryString();

		if (apiData.contains("%20")) {
			apiData = apiData.replace("%20", " ");
		}

		LOGGER.info("apiData: {}", apiData);

		// MD5
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(apiData.getBytes());

		byte byteData[] = md.digest();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}

		String encodedSignature = sb.toString();
		LOGGER.info("Signature: {}", encodedSignature);

		return encodedSignature.equalsIgnoreCase(signature);

	}

	private String getSignatureSecretKey(HttpServletRequest request) {

		return signatureSecretKey;
	}

	@Override
	public void destroy() {
		// empty
	}

	private void acceptRequest(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		long time = System.currentTimeMillis();
		try {
			chain.doFilter(request, response);
		} finally {
			time = System.currentTimeMillis() - time;
			HttpServletRequest hsreq = (HttpServletRequest) request;
			HttpServletResponse hsres = (HttpServletResponse) response;
			Enumeration<String> headerNames = hsreq.getHeaderNames();
			StringBuffer headers = new StringBuffer();
			while (headerNames.hasMoreElements()) {
				String headerName = (String) headerNames.nextElement();
				headers.append(headerName + " : " + hsreq.getHeader(headerName) + ", ");
			}
			MDC.put("header", headers.toString());
			MDC.put("response_status", String.valueOf(hsres.getStatus()));
			MDC.put("time", String.valueOf(time));
			LOGGER.info("Request completed - {} : {} ms ", hsreq.getRequestURI(), time);

			if (hsreq.getRequestURI().contains(("health-check"))) {
				// don't print the log
			} else if (hsreq.getRequestURI().contains(("save-token-time"))) {
				// don't print the log
			} else {
				LOGGER.info("[" + hsreq.getMethod() + "]" + "Request completed - {} : {} ms ", hsreq.getRequestURI(),
						time);
			}

			// MDC.clear();
		}

	}
}
