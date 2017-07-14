package com.challenge.yql.api.weather.security.impl;

import com.challenge.yql.api.weather.security.LoginAttemptService;
import com.challenge.yql.api.weather.service.UserService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by springfield-home on 7/9/17.
 */
@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {
    @Autowired
    private HttpServletRequest request;

    private final int MAX_ATTEMPT = 10;
    private LoadingCache<String, Integer> attemptsCache;
    private Map<String, UserDetails> usersConnected;

    @Autowired
    public LoginAttemptServiceImpl(UserService userService) {
        super();
        attemptsCache = CacheBuilder.newBuilder().
                expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<String, Integer>() {
            public Integer load(String key) {
                return 0;
            }
        });
        usersConnected = new ConcurrentHashMap<>();
    }

    public String loginSucceeded(String key, Authentication authResult) {
        attemptsCache.invalidate(key);
        String token = UUID.randomUUID().toString();
        usersConnected.put(token, (UserDetails) authResult.getPrincipal());
        return token;

    }

    public void loginFailed(String key) {
        int attempts;
        try {
            attempts = attemptsCache.get(key);
        } catch (ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }

    public boolean isBlocked(String key) {
        try {
            return attemptsCache.get(key) >= MAX_ATTEMPT;
        } catch (ExecutionException e) {
            return false;
        }
    }

    public void logout(String token) {
        usersConnected.remove(token);
    }

    public UserDetails userDetailsFromToken(String token) {
        return usersConnected.get(token);
    }
}
