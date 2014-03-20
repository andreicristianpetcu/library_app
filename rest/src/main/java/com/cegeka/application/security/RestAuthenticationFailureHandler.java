package com.cegeka.application.security;


import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        if (exception instanceof DisabledException) {
            response.setStatus(SC_UNAUTHORIZED);
            response.getWriter().write("Email not yet confirmed");
        } else {
            response.setStatus(SC_UNAUTHORIZED);
            response.getWriter().write("Bad credentials");
        }
    }
}
