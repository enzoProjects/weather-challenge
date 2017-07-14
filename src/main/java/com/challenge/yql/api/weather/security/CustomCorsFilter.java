package com.challenge.yql.api.weather.security;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by springfield-home on 7/14/17.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomCorsFilter implements Filter {

    public CustomCorsFilter() {
        super();
    }

    @Override
    public final void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");

        response.setHeader("Access-Control-Allow-Credentials", "true");

        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, Authorization, Origin, Content-Type, Version, Token");
        response.setHeader("Access-Control-Expose-Headers", "X-Requested-With, Authorization, Origin, Content-Type");

        final HttpServletRequest request = (HttpServletRequest) req;
        if (request.getMethod() != "OPTIONS") {
            chain.doFilter(req, res);
        } else {
            //
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
}