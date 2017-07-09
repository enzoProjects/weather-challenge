package com.challenge.yql.api.weather.security;

import org.springframework.stereotype.Service;

/**
 * Created by springfield-home on 7/9/17.
 */
@Service
public interface LoginAttemptService {
    void loginSucceeded(String key);

    void loginFailed(String key);

    boolean isBlocked(String key);
}
