package com.challenge.yql.api.weather.reflection;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by springfield-home on 7/2/17.
 */
public class ReflectionUtils {

    private static final String GETTER_JSON_ELEMENT = "getAs";
    private static final String SET_PREFIX = "set";
    private static final String SET_ID = "setId";


    public static <T> T buildObject(T t, JsonObject json, Predicate<Method> filter) {
        Predicate<Method> setterFilter = (m) -> m.getName().startsWith(SET_PREFIX);
        Predicate<Method> notIdFilter = (m) -> !m.getName().equals(SET_ID);
        Consumer<Method> consumer = (m) -> {
            try {
                String attr = m.getName().substring(SET_PREFIX.length()).toLowerCase();
                JsonElement prim = json.get(attr);
                if (!prim.isJsonNull()) {
                    Method getter = JsonElement.class.getMethod(GETTER_JSON_ELEMENT
                            + StringUtils.capitalize(m.getParameters()[0].getType().getSimpleName()));
                    m.invoke(t, getter.invoke(prim));
                }
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }

        };
        Arrays.stream(t.getClass().getDeclaredMethods())
                .filter(setterFilter.and(setterFilter.and(notIdFilter).and(filter)))
                .forEach(consumer);
        return t;
    }

    public static <T> T buildObject(T t, JsonObject json) {
        return buildObject(t, json, (m) -> true);
    }
}
