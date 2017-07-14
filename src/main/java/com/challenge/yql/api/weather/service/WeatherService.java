package com.challenge.yql.api.weather.service;

import com.challenge.yql.api.weather.model.Weather;
import org.springframework.stereotype.Service;

/**
 * Created by springfield-home on 7/1/17.
 */
@Service
public interface WeatherService {

    /**
     * Query the weather of one particular woeid
     *
     * @param woeid the woeid of the city that we want to know the weather
     * @return Returns the weather information of one city
     */
    Weather queryWeatherByWoeid(Long woeid);

}
