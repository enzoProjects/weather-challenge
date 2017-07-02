package com.challenge.yql.api.weather.model;

import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Created by springfield-home on 7/1/17.
 */
public class Country {
    @Id
    public String id;
    public Long woeid;
    public String countryName;
    public List<Place> places;

    public Country() {
    }
}
