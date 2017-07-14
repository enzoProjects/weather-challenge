package com.challenge.yql.api.weather.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Created by springfield-home on 7/9/17.
 */
@Service
public interface LoginAttemptService {
    String loginSucceeded(String key, Authentication authResult);

    void loginFailed(String key);

    boolean isBlocked(String key);

    UserDetails userDetailsFromToken(String token);

}
