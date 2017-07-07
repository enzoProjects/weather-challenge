package com.challenge.yql.api.weather.service.exception;

/**
 * Created by springfield-home on 7/1/17.
 */
public class ParseObjectFromJsonException extends Exception {
    public ParseObjectFromJsonException(String s, Throwable e) {
        super(s, e);
    }
}
