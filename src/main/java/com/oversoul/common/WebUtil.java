package com.oversoul.common;

import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.http.HttpServletRequest;

public class WebUtil {
    /**
     * Signature filter params
     **/
    public static final String HEADER_PREFIX = "Bearer ";
    public static final String HEADER_PARAM_AUTHORIZATION = "Authorization";
    public static final String HEADER_PARAM_OS_TYPE = "os_type";
    /**
     * Signature filter params end
     **/

    public static final String HEADER_PARAM_APP_ID = "appid";
    public static final String HEADER_PARAM_ROLE_ID = "roleId";
    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";
    private static final String X_REQUESTED_WITH = "X-Requested-With";
    private static final String CONTENT_TYPE = "Content-type";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String HEADER_PARAM_SIGNATURE = "x-signature";
    private static final String HEADER_PARAM_NONCE = "x-nonce";

    public static boolean isAjax(HttpServletRequest request) {
        return XML_HTTP_REQUEST.equals(request.getHeader(X_REQUESTED_WITH));
    }

    public static boolean isAjax(SavedRequest request) {
        return request.getHeaderValues(X_REQUESTED_WITH).contains(XML_HTTP_REQUEST);
    }

    public static boolean isContentTypeJson(SavedRequest request) {
        return request.getHeaderValues(CONTENT_TYPE).contains(CONTENT_TYPE_JSON);
    }

    /**
     * Signature filter params
     **/
    public static String getSignature(HttpServletRequest request) {
        return request.getHeader(HEADER_PARAM_SIGNATURE);
    }

    public static String getNonce(HttpServletRequest request) {
        return request.getHeader(HEADER_PARAM_NONCE);
    }

    public static String getAuthorization(HttpServletRequest request) {
        return request.getHeader(HEADER_PARAM_AUTHORIZATION);
    }

    public static String getOS(HttpServletRequest req) {
        return req.getHeader(HEADER_PARAM_OS_TYPE);
    }

    /**
     * Signature filter params end
     **/

    public static String getAppId(HttpServletRequest request) {
        return request.getHeader(HEADER_PARAM_APP_ID);
    }

    public static String getRoleId(HttpServletRequest request) {
        return request.getHeader(HEADER_PARAM_ROLE_ID);
    }

}
