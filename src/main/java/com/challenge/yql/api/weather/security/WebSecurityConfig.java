package com.challenge.yql.api.weather.security;

import com.challenge.yql.api.weather.security.impl.LogoutHandlerImpl;
import com.challenge.yql.api.weather.security.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Created by springfield-home on 7/8/17.
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Autowired
    private LogoutHandlerImpl logoutHandler;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    TokenAuthenticationFilter tokenAuthenticationFilter;

    @Autowired
    private CustomCorsFilter myCorsFilter;


    @Autowired
    public WebSecurityConfig(@Qualifier("customDetailsService") UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(tokenAuthenticationFilter, BasicAuthenticationFilter.class);
        http
                .logout().addLogoutHandler(logoutHandler).clearAuthentication(true).and()
                .authorizeRequests().antMatchers( "/register", "/login", "/logout", "/api/**", "/update/*").permitAll().and()
                .httpBasic().and()
                .logout().permitAll().and().csrf().disable();
        http
                .formLogin()
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler);

        //CORS
        http.addFilterBefore(myCorsFilter, ChannelProcessingFilter.class);
    }


}
