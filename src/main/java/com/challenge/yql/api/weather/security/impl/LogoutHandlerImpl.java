package com.challenge.yql.api.weather.security.impl;

import com.challenge.yql.api.weather.security.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by springfield-home on 7/13/17.
 */
@Component
public class LogoutHandlerImpl implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        System.out.print("");
    }
}
