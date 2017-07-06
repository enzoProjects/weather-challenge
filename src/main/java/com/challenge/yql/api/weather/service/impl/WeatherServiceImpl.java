package com.challenge.yql.api.weather.service.impl;

import com.challenge.yql.api.weather.model.Weather;
import com.challenge.yql.api.weather.repository.WeatherRepository;
import com.challenge.yql.api.weather.service.WeatherService;
import com.challenge.yql.api.weather.service.exception.ParseObjectFromJsonException;
import com.challenge.yql.client.YqlClient;
import com.challenge.yql.client.YqlQuery;
import com.challenge.yql.client.exception.YqlException;
import com.google.gson.JsonObject;
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

    private static final String WEATHER_FORESCAST_SERVICE = "weather.forecast";
    private static final String WEATHER_FORESCAST_SERVICE_QUERY = "select {0} from {1} where woeid=\"{2}\"";
    private static final int WEATHER_FORESCAST_SERVICE_PARAMS = 3;
    private static final String WILD_CARD = "*";
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
        Weather weather;
        try {
            weather = weatherRepository
                    .findByWoeid(woeid)
                    .orElseThrow(() -> new Exception("could not find woeid: {0} in the database will check on api"));
            logger.debug("weather was found in the db with woeid: {0}", woeid);
            return weather;
        } catch (Exception e) {
            logger.debug(e.getMessage(), woeid);
            try {
                YqlQuery yqlQuery = new YqlQuery(
                        WEATHER_FORESCAST_SERVICE_QUERY,
                        WEATHER_FORESCAST_SERVICE_PARAMS,
                        WILD_CARD,
                        WEATHER_FORESCAST_SERVICE,
                        String.valueOf(woeid));
                yqlQuery.setFormat(YqlQuery.ResultFormat.JSON);
                weather = parseJsonToObjects(yqlClient.query(yqlQuery));
                weather.setWoeid(woeid);
                weatherRepository.save(weather);
                return weather;
            } catch (YqlException | ParseObjectFromJsonException ex) {
                logger.error("error while creating object weather: {0}", ex.getMessage());
                return new Weather();
            }
        }

    }

    /**
     * Parse JsonResponse to a Weather result
     *
     * @param json result of a yqlQuery
     * @return Weather
     * @throws ParseObjectFromJsonException if something goes wrong with the parse
     */
    private Weather parseJsonToObjects(JsonObject json) throws ParseObjectFromJsonException {
        JsonObject jsonPlaces = json
                .getAsJsonObject("query")
                .getAsJsonObject("results")
                .getAsJsonObject("channel");
        return new Weather().buildWeather(jsonPlaces);
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
