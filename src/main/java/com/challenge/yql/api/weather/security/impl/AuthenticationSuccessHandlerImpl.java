package com.challenge.yql.api.weather.security.impl;

import com.challenge.yql.api.weather.security.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by springfield-home on 7/14/17.
 */
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    @Autowired
    private LoginAttemptService loginAttemptService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        String token;
        if (xfHeader == null) {
            token = loginAttemptService.loginSucceeded(request.getRemoteAddr(), authentication);
        } else {
            token = loginAttemptService.loginSucceeded(xfHeader.split(",")[0], authentication);
        }
        response.getWriter().append(token);
        response.addCookie(new Cookie("Token", token));
    }
}
