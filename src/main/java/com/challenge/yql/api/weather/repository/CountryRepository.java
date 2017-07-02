package com.challenge.yql.api.weather.repository;

import com.challenge.yql.api.weather.model.Country;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Created by springfield-home on 7/1/17.
 */
public interface CountryRepository extends MongoRepository<Country, String> {
    Optional<Country> findByWoeid(Long woeid);
}
