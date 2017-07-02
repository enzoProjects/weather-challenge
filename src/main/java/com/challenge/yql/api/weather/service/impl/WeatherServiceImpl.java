package com.challenge.yql.api.weather.service.impl;

import com.challenge.yql.api.weather.model.Weather;
import com.challenge.yql.api.weather.service.WeatherService;
import com.challenge.yql.api.weather.service.exception.ParseObjectFromJson;
import com.challenge.yql.client.YqlClient;
import com.challenge.yql.client.YqlQuery;
import com.challenge.yql.client.exception.YqlException;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by springfield-home on 7/1/17.
 */
@Service
public class WeatherServiceImpl implements WeatherService {

    private static final String WEATHER_FORESCAST_SERVICE = "weather.forecast";
    private static final String WEATHER_FORESCAST_SERVICE_QUERY = "select {0} from {1} where woeid=\"{2}\"";
    private static final int WEATHER_FORESCAST_SERVICE_PARAMS = 3;
    private static final String WILD_CARD = "*";

    private final YqlClient yqlClient;

    @Autowired
    public WeatherServiceImpl(YqlClient yqlClient) {
        this.yqlClient = yqlClient;
    }

    @Override
    public Weather queryWeatherByWoeid(Long woeid) {
        try {
            YqlQuery yqlQuery = new YqlQuery(
                    WEATHER_FORESCAST_SERVICE_QUERY,
                    WEATHER_FORESCAST_SERVICE_PARAMS,
                    WILD_CARD,
                    WEATHER_FORESCAST_SERVICE,
                    String.valueOf(woeid));
            yqlQuery.setFormat(YqlQuery.ResultFormat.JSON);
            return parseJsonToObjects(yqlClient.query(yqlQuery), woeid);
        } catch (YqlException | ParseObjectFromJson e) {
            return new Weather();
        }
    }

    private Weather parseJsonToObjects(JsonObject json, Long woeid) throws ParseObjectFromJson {
        JsonObject jsonPlaces = json
                .getAsJsonObject("query")
                .getAsJsonObject("results")
                .getAsJsonObject("channel");
        return new Weather().buildWeather(jsonPlaces, woeid);
    }
}
