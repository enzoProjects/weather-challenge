package com.challenge.yql.api.weather.service;

import com.challenge.yql.api.weather.model.Weather;
import org.springframework.stereotype.Service;

/**
 * Created by springfield-home on 7/1/17.
 */
@Service
public interface WeatherService {
    Weather queryWeatherByWoeid(Long woeid);
}
