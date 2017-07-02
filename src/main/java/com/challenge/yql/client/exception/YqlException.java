package com.challenge.yql.client.exception;

import com.google.gson.JsonObject;

import java.util.Optional;

/**
 * Created by springfield-home on 7/1/17.
 */
public class YqlException extends Exception {
    private Optional<JsonObject> errorJson;

    public YqlException(String s, Exception e) {
        super(s, e);
    }

    public YqlException(String s, JsonObject jsonResult) {
        super(s);
        this.errorJson = Optional.of(jsonResult);
    }
}
