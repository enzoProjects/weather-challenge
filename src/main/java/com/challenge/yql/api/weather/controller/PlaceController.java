package com.challenge.yql.api.weather.controller;

import com.challenge.yql.api.weather.model.Country;
import com.challenge.yql.api.weather.model.Place;
import com.challenge.yql.api.weather.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @CrossOrigin("*")
    @RequestMapping(value = "/api/place/text/{text}", method = RequestMethod.GET)
    public List<Place> getPlacesByText(@PathVariable String text) {
        return placeService.queryPlacesByText(text);
    }

    @CrossOrigin("*")
    @RequestMapping(value = "/api/place/country/{country}", method = RequestMethod.GET)
    public Country getPlacesByCountry(HttpServletRequest request, @PathVariable String country) {
        return placeService.queryPlacesByCountry(country);
    }


}
