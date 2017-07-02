package com.challenge.yql.api.weather.service;

import com.challenge.yql.api.weather.model.Country;
import com.challenge.yql.api.weather.model.Place;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Enzo on 7/1/17.
 */
@Service
public interface PlaceService {

    /**
     * Query for all the places where the text is on
     *
     * @param text this will contains characters that must be present on the place that you looking for
     * @return A list of places where this text is
     */
    List<Place> queryPlacesByText(String text);

    /**
     * Query for the first appearance of a country just by looking on the string
     *
     * @param country this should contain the country that we are looking for
     * @return Country information with all the cities
     */
    Country queryPlacesByCountry(String country);
}
