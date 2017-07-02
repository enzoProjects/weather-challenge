package com.challenge.yql.api.weather.controller;

import com.challenge.yql.api.weather.model.Country;
import com.challenge.yql.api.weather.model.Place;
import com.challenge.yql.api.weather.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by springfield-home on 7/1/17.
 */
@RestController
public class PlaceController {

    private final PlaceService placeService;

    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @RequestMapping(value = "/place/text/{text}", method = RequestMethod.GET)
    public List<Place> getPlacesByText(@PathVariable String text) {
        return placeService.queryPlacesByText(text);
    }

    @RequestMapping(value = "/place/country/{country}", method = RequestMethod.GET)
    public Country getPlacesByCountry(@PathVariable String country) {
        return placeService.queryPlacesByCountry(country);
    }


}
