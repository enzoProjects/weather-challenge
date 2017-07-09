package com.challenge.yql.api.weather.service.exception;

/**
 * Created by springfield-home on 7/9/17.
 */
public class UserCreationException extends RuntimeException {
    public UserCreationException(String s, Exception ex) {
        super(s, ex);
    }
}
