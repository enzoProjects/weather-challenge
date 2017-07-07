package com.challenge.yql.api.weather.reflection;

import com.challenge.yql.api.weather.reflection.exception.ObjectBuilderException;
import com.google.gson.JsonObject;

/**
 * Created by springfield-home on 7/2/17.
 */
public interface ObjectBuilder {

    <T> T buildObject(JsonObject json, Class<T> clazz) throws ObjectBuilderException;
}
