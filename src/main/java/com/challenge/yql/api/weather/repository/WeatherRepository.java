package com.challenge.yql.api.weather.repository;

import com.challenge.yql.api.weather.model.Weather;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Created by springfield-home on 7/1/17.
 */
public interface WeatherRepository extends MongoRepository<Weather, String> {
    Optional<Weather> findByWoeid(Long woeid);
}
