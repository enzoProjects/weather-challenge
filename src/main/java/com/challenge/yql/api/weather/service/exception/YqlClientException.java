package com.challenge.yql.api.weather.service.exception;

/**
 * Created by springfield-home on 7/9/17.
 */
public class YqlClientException extends RuntimeException {
    public YqlClientException(String message, Exception ex) {
        super(message, ex);
    }
}
