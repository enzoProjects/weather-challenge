package com.challenge.yql.api.weather.service.impl;

import com.challenge.yql.api.weather.model.Country;
import com.challenge.yql.api.weather.model.Place;
import com.challenge.yql.api.weather.reflection.ReflectionUtils;
import com.challenge.yql.api.weather.repository.CountryRepository;
import com.challenge.yql.api.weather.service.PlaceService;
import com.challenge.yql.api.weather.service.exception.ParseObjectFromJson;
import com.challenge.yql.client.YqlClient;
import com.challenge.yql.client.YqlQuery;
import com.challenge.yql.client.exception.YqlException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by springfield-home on 7/1/17.
 */
@Service
public class PlaceServiceImpl implements PlaceService {


    private static final String WILD_CARD = "*";
    private static final String GEO_PLACES_SERVICE = "geo.places";
    private static final String GEO_PLACES_CHILDREN_SERVICE = "geo.places.children";
    private static final int GEO_PLACES_SERVICE_PARAMS = 3;
    private static final int GEO_PLACES_CHILDREN_SERVICE_PARAMS = 3;
    private static final String GEO_PLACES_SERVICE_QUERY = "select {0} from {1} where text=\"{2}\"";
    private static final String GEO_PLACES_CHILDREN_SERVICE_QUERY = "select {0} from {1} where parent_woeid=\"{2}\"";

    private final YqlClient yqlClient;

    private final CountryRepository countryRepository;

    @Autowired
    public PlaceServiceImpl(CountryRepository countryRepository, YqlClient yqlClient) {
        this.yqlClient = yqlClient;
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Place> queryPlacesByText(String text) {
        return query(
                GEO_PLACES_SERVICE_QUERY,
                GEO_PLACES_SERVICE_PARAMS,
                WILD_CARD,
                GEO_PLACES_SERVICE,
                text);
    }

    @Override
    public Country queryPlacesByCountry(String sCountry) {

        Place place = queryPlacesByText(sCountry)
                .stream()
                .filter(Place::isCountry)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Imposible to find any country by that name"));
        Country country;
        try {
            country = countryRepository
                    .findByWoeid(place.getWoeid())
                    .orElseThrow(() -> new Exception("could not find country on DB prociding to query"));
            return country;
        } catch (Exception e) {
            List<Place> response = query(
                    GEO_PLACES_CHILDREN_SERVICE_QUERY,
                    GEO_PLACES_CHILDREN_SERVICE_PARAMS,
                    WILD_CARD,
                    GEO_PLACES_CHILDREN_SERVICE,
                    String.valueOf(place.getWoeid()));
            country = new Country();
            country.places = filterCountriesOut(response.stream());
            country.woeid = place.getWoeid();
            country.countryName = place.getPrettyName();
            countryRepository.save(country);
            return country;
        }
    }

    public List<String> queryListOfCountries() {
        return null;
    }

    private List<Place> filterCountriesOut(Stream<Place> stream) {
        return stream
                .filter((p) -> !p.isCountry())
                .collect(Collectors.toList());
    }

    private List<Place> query(String query, int n, String... params) {
        YqlQuery yqlQuery = new YqlQuery(
                query,
                n,
                params);
        yqlQuery.setFormat(YqlQuery.ResultFormat.JSON);
        try {
            return parseJsonToObjects(yqlClient.query(yqlQuery));
        } catch (YqlException | ParseObjectFromJson parseObjectFromJsone) {
            return new LinkedList<>();
        }
    }

    private List<Place> parseJsonToObjects(JsonObject json) throws ParseObjectFromJson {
        try {
            JsonElement jsonPlaces = json
                    .getAsJsonObject("query")
                    .getAsJsonObject("results")
                    .get("place");
            List<Place> places = new LinkedList<>();
            if (jsonPlaces.isJsonArray()) {
                for (JsonElement jsonPlace : jsonPlaces.getAsJsonArray()) {
                    places.add(ReflectionUtils
                            .buildObject(new Place(), jsonPlace.getAsJsonObject())
                            .buildPrettyName());
                }
            } else if (jsonPlaces.isJsonObject()) {
                places.add(ReflectionUtils
                        .buildObject(new Place(), jsonPlaces.getAsJsonObject())
                        .buildPrettyName());
            } else {
                throw new Exception("Result is not as expected");
            }
            return places;
        } catch (Exception e) {
            throw new ParseObjectFromJson("Problem parsing object from json: " + e.getLocalizedMessage(), e);
        }
    }


}
