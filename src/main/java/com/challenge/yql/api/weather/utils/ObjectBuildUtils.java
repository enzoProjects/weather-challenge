package com.challenge.yql.api.weather.utils;

import com.challenge.yql.api.weather.service.exception.ParseObjectFromJsonException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by springfield-home on 7/7/17.
 */
public class ObjectBuildUtils {

    private ObjectBuildUtils() {
    }

    public static JsonElement extractJsonElementFromJson(JsonObject json, String ... paths) throws ParseObjectFromJsonException {
        try {
            int n = paths.length - 1;
            for(int i = 0; i < n; i++) {
                json = json.getAsJsonObject(paths[i]);
            }
            return json.get(paths[n]);
        } catch (NullPointerException ex) {
            throw new ParseObjectFromJsonException("There was no path in the object: " + ex.getMessage(), ex);
        }

    }
}