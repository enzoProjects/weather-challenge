package com.challenge.yql.api.weather.security.impl;

import com.challenge.yql.api.weather.security.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by springfield-home on 7/14/17.
 */
@Component
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            loginAttemptService.loginFailed(request.getRemoteAddr());
        } else {
            loginAttemptService.loginFailed(xfHeader.split(",")[0]);
        }
        response.getWriter().append("ERROR");
    }
}
