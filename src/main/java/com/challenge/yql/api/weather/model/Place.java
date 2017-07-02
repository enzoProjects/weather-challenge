package com.challenge.yql.api.weather.model;

import com.google.gson.JsonObject;
import org.springframework.data.annotation.Transient;

/**
 * Created by springfield-home on 7/1/17.
 */
public class Place {
    public static final String GETTER_JSON_ELEMENT = "getAs";
    public static final String SET_ID = "setId";
    public static final String SET_PREFIX = "set";

    private Long woeid;
    private String prettyName;
    @Transient
    private JsonObject country;
    @Transient
    private JsonObject admin1;
    @Transient
    private JsonObject admin2;
    @Transient
    private JsonObject admin3;
    @Transient
    private JsonObject locality1;
    @Transient
    private JsonObject locality2;
    private boolean isCountry;

    public Place() {
    }

    public Place buildPrettyName() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.country.get("content").getAsString());
        if (!(admin1 == null)) {
            sb.append(", " + this.admin1.get("content").getAsString());
            if (!(admin2 == null)) sb.append(", " + this.admin2.get("content").getAsString());
            if (!(admin3 == null)) sb.append(", " + this.admin3.get("content").getAsString());
            if (!(locality1 == null)) sb.append(", " + this.locality1.get("content").getAsString());
            if (!(locality2 == null)) sb.append(", " + this.locality2.get("content").getAsString());
        } else {
            isCountry = true;
        }
        this.prettyName = sb.toString();
        return this;
    }

    public Long getWoeid() {
        return woeid;
    }

    public void setWoeid(Long woeid) {
        this.woeid = woeid;
    }

    public void setAdmin1(JsonObject admin1) {
        this.admin1 = admin1;
    }

    public void setAdmin2(JsonObject admin2) {
        this.admin2 = admin2;
    }

    public void setAdmin3(JsonObject admin3) {
        this.admin3 = admin3;
    }

    public String getPrettyName() {
        return prettyName;
    }

    public void setLocality1(JsonObject locality1) {
        this.locality1 = locality1;
    }

    public void setLocality2(JsonObject locality2) {
        this.locality2 = locality2;
    }

    public boolean isCountry() {
        return isCountry;
    }

    public void setCountry(JsonObject country) {
        this.country = country;
    }

}


