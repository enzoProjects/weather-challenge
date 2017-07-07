package com.challenge.yql.api.weather.reflection.impl;

import com.challenge.yql.api.weather.annotation.ObjectBuilderProperty;
import com.challenge.yql.api.weather.reflection.ObjectBuilder;
import com.challenge.yql.api.weather.reflection.exception.ObjectBuilderException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by springfield-home on 7/2/17.
 */
public class ObjectBuilderImpl implements ObjectBuilder {

    private final Logger logger = LoggerFactory.getLogger(ObjectBuilderImpl.class);

    private static final Predicate<Method> FILTER_ANNOTATION = (m) -> m.getAnnotation(ObjectBuilderProperty.class) == null;
    private static final Predicate<Method> FILTER_TRANSIENT = (m) -> !("transient".equals(m.getAnnotation(ObjectBuilderProperty.class).value()));

    private static final String GETTER_JSON_ELEMENT = "getAs";
    private static final String SET_PREFIX = "set";

    private DateTimeFormatter formatter;
    private Predicate<Method> filter;


    public ObjectBuilderImpl() {
    }


    public <T> T buildObject(JsonObject json, Class<T> clazz) throws ObjectBuilderException {
        final T object;
        try {
            object = clazz.getConstructor().newInstance();
        } catch (Throwable e) {
            logger.error("Error doing a new instance of class: " + clazz);
            throw new ObjectBuilderException(e.getMessage(), e);
        }
        Predicate<Method> filter = FILTER_ANNOTATION.or(FILTER_TRANSIENT);
        Predicate<Method> filterSetter = (m)-> m.getName().startsWith(SET_PREFIX);

        if(this.filter != null) {
            filter = filter.and(this.filter);
        }
        Consumer<Method> setField = (m) -> {
            try {
                String attr = m.getName().substring(SET_PREFIX.length()).toLowerCase();
                JsonElement prim = json.get(attr);
                if (!prim.isJsonNull()) {
                    String clazzName = m.getParameters()[0].getType().getSimpleName();
                    switch (clazzName) {
                        case "LocalDate":
                            m.invoke(object, LocalDate.parse(prim.getAsString(), formatter));
                            break;
                        case "JsonElement":
                            m.invoke(object, prim);
                            break;
                        default:
                            Method getter = JsonElement.class.getMethod(GETTER_JSON_ELEMENT
                                    + clazzName);
                            m.invoke(object, getter.invoke(prim));
                            break;
                    }
                }
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }

        };

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(filterSetter.and(filter))
                .forEach(setField);
        return object;
    }

    public ObjectBuilder setFormatter(DateTimeFormatter formatter) {
        this.formatter = formatter;
        return this;
    }

    public ObjectBuilder setFilter(Predicate<Method> filter) {
        this.filter = filter;
        return this;
    }

}
