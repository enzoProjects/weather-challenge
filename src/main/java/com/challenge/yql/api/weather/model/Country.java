package com.challenge.yql.api.weather.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by springfield-home on 7/1/17.
 */
@Document
public class Country {
    public Place countryInfo;
    public List<Place> childrens;


}
