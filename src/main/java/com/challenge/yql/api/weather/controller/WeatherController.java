package com.challenge.yql.api.weather.controller;

import com.challenge.yql.api.weather.model.Weather;
import com.challenge.yql.api.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by springfield-home on 7/1/17.
 */
@RestController
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @CrossOrigin("*")
    @RequestMapping(value = "/api/weather/woeid/{woeid}", method = RequestMethod.GET)
    public Weather getWeatherByWoeid(@PathVariable Long woeid) {
        return weatherService.queryWeatherByWoeid(woeid);
    }


}
