package com.challenge.yql.api.weather.service.impl;

import com.challenge.yql.api.weather.model.Weather;
import com.challenge.yql.api.weather.repository.WeatherRepository;
import com.challenge.yql.api.weather.service.WeatherService;
import com.challenge.yql.api.weather.service.exception.ParseObjectFromJsonException;
import com.challenge.yql.api.weather.utils.ObjectBuildUtils;
import com.challenge.yql.client.YqlClient;
import com.challenge.yql.client.YqlQuery;
import com.challenge.yql.client.exception.YqlException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by springfield-home on 7/1/17.
 */
@Service
public class WeatherServiceImpl implements WeatherService {

    private static final String WEATHER_FORECAST_SERVICE = "weather.forecast";
    private static final String WEATHER_FORECAST_SERVICE_QUERY = "select {0} from {1} where woeid=\"{2}\"";
    private static final int WEATHER_FORECAST_SERVICE_PARAMS = 3;
    private static final String WILD_CARD = "*";
    private static final String[] JSON_PATH_FORECAST = {"query", "results", "channel", "item"};


    private final YqlClient yqlClient;
    private final WeatherRepository weatherRepository;
    Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    @Autowired
    public WeatherServiceImpl(YqlClient yqlClient, WeatherRepository weatherRepository) {
        this.yqlClient = yqlClient;
        this.weatherRepository = weatherRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Weather queryWeatherByWoeid(Long woeid) {
        Weather weather = new Weather();
        try {
            weather = weatherRepository
                    .findByWoeid(woeid)
                    .orElseThrow(() -> new Exception("could not find woeid: {} in the database will check on api"));
            logger.debug("weather was found in the db with woeid: {}", woeid);
            return weather;
        } catch (Exception e) {
            logger.debug(e.getMessage(), woeid);
            try {
                YqlQuery yqlQuery = new YqlQuery(
                        WEATHER_FORECAST_SERVICE_QUERY,
                        WEATHER_FORECAST_SERVICE_PARAMS,
                        WILD_CARD,
                        WEATHER_FORECAST_SERVICE,
                        String.valueOf(woeid));
                yqlQuery.setFormat(YqlQuery.ResultFormat.JSON);
                weather = parseJsonToObject(yqlClient.query(yqlQuery), weather);
                weather.woeid = woeid;
                weatherRepository.save(weather);
                return weather;
            } catch (YqlException | ParseObjectFromJsonException ex) {
                logger.error("error while creating object weather: {}", ex.getMessage());
                return weather;
            }
        }

    }

    /**
     * Parse JsonResponse to a Weather
     *
     * @param json result of a yqlQuery
     * @return Weather
     * @throws ParseObjectFromJsonException if something goes wrong with the parse
     */
    private Weather parseJsonToObject(JsonObject json, Weather weather) throws ParseObjectFromJsonException {
        try {
            Gson gson = new GsonBuilder()
                    .serializeNulls()
                    .setPrettyPrinting()
                    .setDateFormat("EEE, dd MMM YYYY hh:mm a z")
                    .create();

            JsonObject jsonForecasts = ObjectBuildUtils.extractJsonElementFromJson(json, JSON_PATH_FORECAST).getAsJsonObject();

            weather.item = gson.fromJson(jsonForecasts, Weather.Forecast.class);
            return weather;
        } catch (JsonSyntaxException e) {
            throw new ParseObjectFromJsonException("Problem parsing object from json: " + e.getLocalizedMessage(), e);
        }
    }

    /**
     * Task for update the weather that is on the db once per minute
     */
    @Scheduled(fixedDelay = 60_000)
    private void checkWeatherOnDbTask() {
        weatherRepository.findAll().forEach((w) -> {
            System.out.println(w);
        });
    }
}
