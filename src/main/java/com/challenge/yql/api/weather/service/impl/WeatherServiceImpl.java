package com.challenge.yql.api.weather.service.impl;

import com.challenge.yql.api.weather.model.Weather;
import com.challenge.yql.api.weather.repository.WeatherRepository;
import com.challenge.yql.api.weather.service.WeatherService;
import com.challenge.yql.api.weather.service.exception.YqlClientException;
import com.challenge.yql.api.weather.utils.ObjectBuildUtils;
import com.challenge.yql.api.weather.utils.ObjectUtilsException;
import com.challenge.yql.api.weather.websocket.UpdateWeatherHandler;
import com.challenge.yql.client.YqlClient;
import com.challenge.yql.client.YqlQuery;
import com.challenge.yql.client.exception.YqlException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.sun.javaws.exceptions.CacheAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

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

    private static final Map<Long, Set<String>> MAP_FOR_UPDATE_WEBSOCKET = new ConcurrentHashMap<>();

    private static final Queue<Long> WEATHER_FOR_UPDATE = new ConcurrentLinkedQueue<>();

    private final UpdateWeatherHandler updateWeatherHandler;
    private final YqlClient yqlClient;
    private final WeatherRepository weatherRepository;
    Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    @Autowired
    public WeatherServiceImpl(YqlClient yqlClient, WeatherRepository weatherRepository, UpdateWeatherHandler updateWeatherHandler) {
        this.yqlClient = yqlClient;
        this.weatherRepository = weatherRepository;
        this.updateWeatherHandler = updateWeatherHandler;
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
                    .orElseThrow(() -> new NoSuchElementException("could not find woeid: {} in the database will check on api"));
            logger.debug("weather was found in the db with woeid: {}", woeid);
            return weather;
        } catch (Exception e) {
            logger.debug(e.getMessage(), woeid);
            weather = httpQueryWeatherByWoeid(woeid);
            weatherRepository.save(weather);
            return httpQueryWeatherByWoeid(woeid);
        }

    }

    private Weather httpQueryWeatherByWoeid(Long woeid) {
        try {
            Weather weather = new Weather();
            YqlQuery yqlQuery = new YqlQuery(
                    WEATHER_FORECAST_SERVICE_QUERY,
                    WEATHER_FORECAST_SERVICE_PARAMS,
                    WILD_CARD,
                    WEATHER_FORECAST_SERVICE,
                    String.valueOf(woeid));
            yqlQuery.setFormat(YqlQuery.ResultFormat.JSON);
            weather = parseJsonToObject(yqlClient.query(yqlQuery), weather);
            weather.woeid = woeid;
            return weather;
        } catch (YqlException queryEx) {
            logger.error("Query error: {}", queryEx.getMessage());
            throw new YqlClientException("Problem in the query: " + queryEx.getMessage(), queryEx);
        } catch (JsonSyntaxException parseEx) {
            logger.error("Parse error: {}", parseEx.getMessage());
            throw new YqlClientException("Problem in the query: " + parseEx.getMessage(), parseEx);
        }
    }

    public static void addWeatherToUpdate(Long woeid, String username) {
        if (UpdateWeatherHandler.isActiveUser(username)) {
            Set<String> users = MAP_FOR_UPDATE_WEBSOCKET.get(woeid);
            if (users == null) {
                users = ConcurrentHashMap.newKeySet();
                users.add(username);
                MAP_FOR_UPDATE_WEBSOCKET.put(woeid, users);
            }
            users.add(username);
        }
    }

    public static void removeWeatherToUpdate(Long woeid, String username) {
        if (UpdateWeatherHandler.isActiveUser(username)) {
            Set<String> users = MAP_FOR_UPDATE_WEBSOCKET.get(woeid);
            if (users != null) {
                users.remove(username);
            }
        }
    }


    /**
     * Parse JsonResponse to a Weather
     *
     * @param json result of a yqlQuery
     * @return Weather
     */
    private Weather parseJsonToObject(JsonObject json, Weather weather) {
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .setDateFormat("EEE, dd MMM YYYY hh:mm a z")
                .create();

        JsonObject jsonForecasts;
        try {
            jsonForecasts = ObjectBuildUtils.extractJsonElementFromJson(json, JSON_PATH_FORECAST).getAsJsonObject();
        } catch (ObjectUtilsException respEx) {
            logger.error("Response from client error: {}", respEx.getMessage());
            throw new YqlClientException("Problem in the response: " + respEx.getMessage(), respEx);
        }

        weather.item = gson.fromJson(jsonForecasts, Weather.Forecast.class);
        return weather;

    }

    @Async
    private void sendUpdates() {
        while(!WEATHER_FOR_UPDATE.isEmpty()) {
            Long woeid = WEATHER_FOR_UPDATE.poll();
            updateWeatherHandler.sendWeatherUpdate(woeid, MAP_FOR_UPDATE_WEBSOCKET.get(woeid));
        }
    }

    /**
     * Task for update the weather that is on the db once per minute
     */
    @Scheduled(fixedDelay = 60_000)
    private void checkWeatherOnDbTask() {
        weatherRepository.findAll().forEach((w) -> {
            Weather weatherToUpdate = this.httpQueryWeatherByWoeid(w.woeid);
            if (!weatherToUpdate.equals(w)) {
                weatherToUpdate.id = w.id;
                weatherRepository.save(weatherToUpdate);
                WEATHER_FOR_UPDATE.add(weatherToUpdate.woeid);
            }
        });
        sendUpdates();
    }


}
