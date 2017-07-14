package com.challenge.yql.api.weather.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by springfield-home on 7/13/17.
 */
@Component
public class TokenAuthenticationFilter extends GenericFilterBean {

    private final LoginAttemptService loginAttemptService;

    @Autowired
    public TokenAuthenticationFilter(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;

    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;

        //extract token from header
        final String accessToken = httpRequest.getHeader("Token");
        if (null != accessToken) {
            final UserDetails user = loginAttemptService.userDetailsFromToken(accessToken);
            if(user != null) {
                final UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }


}