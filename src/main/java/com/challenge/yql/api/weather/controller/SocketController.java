package com.challenge.yql.api.weather.controller;

import com.challenge.yql.api.weather.model.Weather;
import com.challenge.yql.api.weather.model.WeatherUpdate;
import org.springframework.messaging.handler.annotation.SendTo;

/**
 * Created by springfield-home on 7/2/17.
 */
public class SocketController {

    @SendTo("/weather/updates")
    public WeatherUpdate weatherUpdate(Weather weather) throws Exception {
        return new WeatherUpdate(weather.getLastBuildDate() + " " + weather.getWoeid());
    }
}
