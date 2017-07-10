package com.challenge.yql.api.weather.controller;

import com.challenge.yql.api.weather.model.Country;
import com.challenge.yql.api.weather.model.Place;
import com.challenge.yql.api.weather.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by springfield-home on 7/1/17.
 */
@RestController
public class PlaceController {

    @Autowired
    HttpServletRequest request;

    private final PlaceService placeService;

    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @RequestMapping(value = "/api/place/text/{text}", method = RequestMethod.GET)
    public List<Place> getPlacesByText(@PathVariable String text) {
        System.out.println(request);
        return placeService.queryPlacesByText(text);
    }

    @RequestMapping(value = "/api/place/country/{country}", method = RequestMethod.GET)
    public Country getPlacesByCountry(HttpServletRequest request, @PathVariable String country) {
        System.out.println(request);

        return placeService.queryPlacesByCountry(country);
    }


}
