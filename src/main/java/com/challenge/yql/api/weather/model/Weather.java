package com.challenge.yql.api.weather.model;

import com.challenge.yql.api.weather.model.subweather.*;
import com.challenge.yql.api.weather.reflection.ReflectionUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by springfield-home on 7/1/17.
 */
public class Weather {
    @Id
    private String id;
    private Long woeid;
    private Date lastBuildDate;
    private Units units;
    private Wind wind;
    private Atmosphere atmosphere;
    private Astronomy astronomy;
    private List<Forecast> forecasts;

    public Weather() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Atmosphere getAtmosphere() {
        return atmosphere;
    }

    public void setAtmosphere(Atmosphere atmosphere) {
        this.atmosphere = atmosphere;
    }

    public Astronomy getAstronomy() {
        return astronomy;
    }

    public void setAstronomy(Astronomy astronomy) {
        this.astronomy = astronomy;
    }

    public List<Forecast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<Forecast> forecasts) {
        this.forecasts = forecasts;
    }

    public Date getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(Date lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public Units getUnits() {
        return units;
    }

    public void setUnits(Units units) {
        this.units = units;
    }

    public Long getWoeid() {
        return woeid;
    }

    public void setWoeid(Long woeid) {
        this.woeid = woeid;
    }

    public Weather buildWeather(JsonObject jsonObject, Long woeid) {
        this.wind = ReflectionUtils.buildObject(new Wind(), jsonObject.getAsJsonObject("wind"));
        this.astronomy = ReflectionUtils.buildObject(new Astronomy(), jsonObject.getAsJsonObject("astronomy"));
        this.atmosphere = ReflectionUtils.buildObject(new Atmosphere(), jsonObject.getAsJsonObject("atmosphere"));
        JsonElement jsonForecasts = jsonObject
                .getAsJsonObject("item")
                .get("forecast");
        forecasts = new LinkedList<>();
        if (jsonForecasts.isJsonArray()) {
            jsonForecasts
                    .getAsJsonArray()
                    .forEach((f) -> {
                        forecasts.add(ReflectionUtils.buildObject(new Forecast(), f.getAsJsonObject()));
                    });
        } else {
            forecasts.add(ReflectionUtils.buildObject(new Forecast(), jsonForecasts.getAsJsonObject()));
        }
        this.woeid = woeid;
        return this;
    }
}