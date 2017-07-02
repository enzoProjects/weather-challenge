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

    List<Place> queryPlacesByText(String text);

    Country queryPlacesByCountry(String country);
}
