package com.cegeka.application.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CrossDomainRequestFilter extends OncePerRequestFilter {

    private static Logger logger = LoggerFactory.getLogger(CrossDomainRequestFilter.class);

    private FilterConfig filterConfig = null;
    private String allowOrigin;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (clientChecksIfPathIsAccessibleViaCors(request)) {
            replyThatCorsIsEnabled(response, request.getHeader("Origin"));
            return;
        }else if(regularCorsCall(request)){
            allowCredentialsFromOrigin(response, request.getHeader("Origin"));
        }
        filterChain.doFilter(request, response);

    }

    private void replyThatCorsIsEnabled(HttpServletResponse response, String origin) {
        // CORS "pre-flight" request
        allowCredentialsFromOrigin(response, origin);
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
        response.addHeader("Access-Control-Max-Age", "1");// 30 min
        response.setStatus(200);
    }

    private void allowCredentialsFromOrigin(HttpServletResponse response, String origin) {
        if (allowOrigin.contains(origin)) {
            response.addHeader("Access-Control-Allow-Origin", origin);
            response.addHeader("Access-Control-Allow-Credentials", "true");
        }
    }

    private boolean regularCorsCall(HttpServletRequest request) {
        return request.getHeader("Origin") != null;
    }

    private boolean clientChecksIfPathIsAccessibleViaCors(HttpServletRequest request) {
        return "OPTIONS".equals(request.getMethod()) && request.getHeader("Access-Control-Request-Method") != null;
    }

    protected void initFilterBean() throws ServletException {
        allowOrigin = getFilterConfig().getInitParameter("allow-origin");
        if(allowOrigin == null){
            allowOrigin = "*";
        }
    }
}