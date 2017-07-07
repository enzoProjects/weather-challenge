package com.challenge.yql.api.weather.model;

/**
 * Created by springfield-home on 7/2/17.
 */
public class WeatherUpdate {
    private String message;

    public WeatherUpdate(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
