package com.challenge.yql.api.weather.model;

import com.challenge.yql.api.weather.annotation.ObjectBuilderProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.JsonElement;

/**
 * Created by springfield-home on 7/1/17.
 */
public class Place {
    private Long woeid;

    private JsonElement country;
    private JsonElement admin1;
    private JsonElement admin2;
    private JsonElement admin3;
    private JsonElement locality1;
    private JsonElement locality2;
    private boolean bCountry;


    public Place() {
        bCountry = true;
    }

    public String getPrettyName() {
        StringBuilder prettyName = new StringBuilder();
        append(prettyName, country);
        if (!bCountry) {
            append(prettyName, admin1, admin2, admin3, locality1, locality2);
        }
        return prettyName.toString();
    }

    private void append(StringBuilder prettyName, JsonElement... objects) {
        for (JsonElement obj : objects) {
            if (!obj.isJsonNull()) {
                prettyName
                        .append(", ")
                        .append(obj.getAsJsonObject().get("content").getAsString());
            }
        }
    }

    public Long getWoeid() {
        return woeid;
    }

    public void setWoeid(Long woeid) {
        this.woeid = woeid;
    }

    public JsonElement getCountry() {
        return country;
    }

    public void setCountry(JsonElement country) {
        this.country = country;
    }

    public JsonElement getAdmin1() {
        return admin1;
    }

    public void setAdmin1(JsonElement admin1) {
        this.bCountry = false;
        this.admin1 = admin1;
    }

    public JsonElement getAdmin2() {
        return admin2;
    }

    public void setAdmin2(JsonElement admin2) {
        this.admin2 = admin2;
    }

    public JsonElement getAdmin3() {
        return admin3;
    }

    public void setAdmin3(JsonElement admin3) {
        this.admin3 = admin3;
    }

    public JsonElement getLocality1() {
        return locality1;
    }

    public void setLocality1(JsonElement locality1) {
        this.locality1 = locality1;
    }

    public JsonElement getLocality2() {
        return locality2;
    }

    public void setLocality2(JsonElement locality2) {
        this.locality2 = locality2;
    }

    public boolean isbCountry() {
        return bCountry;
    }

    @ObjectBuilderProperty(value = "transient")
    public void setbCountry(boolean bCountry) {
        this.bCountry = bCountry;
    }
}


