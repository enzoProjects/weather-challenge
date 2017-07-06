package com.challenge.yql.api.weather.model;

import com.challenge.yql.api.weather.annotation.ObjectBuilderProperty;
import com.challenge.yql.api.weather.model.subweather.*;
import com.challenge.yql.api.weather.reflection.ObjectBuilder;
import com.challenge.yql.api.weather.reflection.exception.ObjectBuilderException;
import com.challenge.yql.api.weather.reflection.impl.ObjectBuilderImpl;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import springfox.documentation.spring.web.json.Json;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by springfield-home on 7/1/17.
 */
@Document
public class Weather {
    @Id
    private String id;
    private Long woeid;
    private LocalDate lastBuildDate;
    private JsonElement units;
    private JsonElement wind;
    private JsonElement atmosphere;
    private JsonElement astronomy;
    private JsonArray forecasts;

    public Weather() {
    }

    public String getId() {
        return id;
    }

    @ObjectBuilderProperty(value = "transient")
    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(LocalDate lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public Long getWoeid() {
        return woeid;
    }

    @ObjectBuilderProperty(value = "transient")
    public void setWoeid(Long woeid) {
        this.woeid = woeid;
    }

    public Weather buildWeather(JsonObject jsonObject) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ObjectBuilder objectBuilder = new ObjectBuilderImpl().setFormatter(dateFormatter);
        try {
            this.wind = objectBuilder.buildObject(jsonObject.getAsJsonObject("wind").getAsJsonObject(), Wind.class);
            this.astronomy = objectBuilder.buildObject(jsonObject.getAsJsonObject("astronomy").getAsJsonObject(), Astronomy.class);
            this.atmosphere = objectBuilder.buildObject(jsonObject.getAsJsonObject("atmosphere").getAsJsonObject(), Atmosphere.class);
            JsonElement jsonForecasts = jsonObject
                    .getAsJsonObject("item")
                    .get("forecast");
            forecasts = new LinkedList<>();

            if (jsonForecasts.isJsonArray()) {
                for (JsonElement jsonPlace : jsonForecasts.getAsJsonArray()) {
                    forecasts.add(objectBuilder.buildObject(jsonPlace.getAsJsonObject(), Forecast.class));
                }
            } else {
                forecasts.add(objectBuilder.buildObject(jsonForecasts.getAsJsonObject(), Forecast.class));
            }
        } catch (ObjectBuilderException ex) {
        }
        return this;
    }
}