package com.challenge.yql.api.weather.service.impl;

import com.challenge.yql.api.weather.model.Country;
import com.challenge.yql.api.weather.model.Place;
import com.challenge.yql.api.weather.repository.CountryRepository;
import com.challenge.yql.api.weather.service.PlaceService;
import com.challenge.yql.api.weather.service.exception.YqlClientException;
import com.challenge.yql.api.weather.utils.ObjectBuildUtils;
import com.challenge.yql.api.weather.utils.ObjectUtilsException;
import com.challenge.yql.client.YqlClient;
import com.challenge.yql.client.YqlQuery;
import com.challenge.yql.client.exception.YqlException;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


    public static final String[] JSON_PATH = {"query", "results", "place"};
    private static final String WILD_CARD = "*";
    private static final String GEO_PLACES_SERVICE = "geo.places";
    private static final String GEO_PLACES_CHILDREN_SERVICE = "geo.places.children";
    private static final int GEO_PLACES_SERVICE_PARAMS = 3;
    private static final int GEO_PLACES_CHILDREN_SERVICE_PARAMS = 3;
    private static final String GEO_PLACES_SERVICE_QUERY = "select {0} from {1} where text=\"{2}\"";
    private static final String GEO_PLACES_CHILDREN_SERVICE_QUERY = "select {0} from {1} where parent_woeid=\"{2}\"";
    private final YqlClient yqlClient;
    private final CountryRepository countryRepository;
    Logger logger = LoggerFactory.getLogger(PlaceServiceImpl.class);

    @Autowired
    public PlaceServiceImpl(CountryRepository countryRepository, YqlClient yqlClient) {
        this.yqlClient = yqlClient;
        this.countryRepository = countryRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Place> queryPlacesByText(String text) {
        return query(
                GEO_PLACES_SERVICE_QUERY,
                GEO_PLACES_SERVICE_PARAMS,
                WILD_CARD,
                GEO_PLACES_SERVICE,
                text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Country queryPlacesByCountry(String sCountry) {
        Place place = null;
        Country country;
        try {
            place = queryPlacesByText(sCountry)
                    .stream()
                    .filter(Place::isbCountry)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Imposible to find any country by that name"));
            country = countryRepository
                    .findByCountryInfoWoeid(place.woeid)
                    .orElseThrow(() -> new Exception("Could not find country {} in db prociding to query"));
            logger.debug("Country was found in the db with woeid: {}", country.countryInfo.woeid);
            return country;
        } catch (IllegalArgumentException ex) {
            logger.error("Error while finding the woeid with the country name: {}", ex.getMessage());
            return new Country();
        } catch (Exception e) {
            logger.debug(e.getMessage(), place.getPrettyName());
            List<Place> response = query(
                    GEO_PLACES_CHILDREN_SERVICE_QUERY,
                    GEO_PLACES_CHILDREN_SERVICE_PARAMS,
                    WILD_CARD,
                    GEO_PLACES_CHILDREN_SERVICE,
                    String.valueOf(place.woeid));
            country = new Country();
            country.childrens = filterCountriesOut(response.stream());
            country.countryInfo = place;
            countryRepository.save(country);
            return country;
        }
    }

    public List<String> queryListOfCountries() {
        return null;
    }

    /**
     * Filter to get only with the results that are not countries
     *
     * @param stream Stream of places
     * @return List with all the places that are not countries
     */
    private List<Place> filterCountriesOut(Stream<Place> stream) {
        return stream
                .filter((p) -> !p.isbCountry())
                .collect(Collectors.toList());
    }

    /**
     * Query for the information given by the query
     *
     * @param query  the format for the query
     * @param n      the number of parameter that the query will have
     * @param params the parameters of the query
     * @return List of places that were found with that query or an empty list if there was a problem
     */
    private List<Place> query(String query, int n, String... params) {
        YqlQuery yqlQuery = new YqlQuery(
                query,
                n,
                params);
        yqlQuery.setFormat(YqlQuery.ResultFormat.JSON);
        try {
            return parseJsonToObjects(yqlClient.query(yqlQuery));
        } catch (YqlException queryEx) {
            logger.error("Query error: {}", queryEx.getMessage());
            throw new YqlClientException("Problem in the query: " + queryEx.getMessage(), queryEx);
        } catch (JsonSyntaxException parseEx) {
            logger.error("Parse error: {}", parseEx.getMessage());
            throw new YqlClientException("Problem in the query: " + parseEx.getMessage(), parseEx);
        }
    }

    /**
     * Parse JsonResponse to a list of weather
     *
     * @param json result of a yqlQuery
     * @return Weather
     */
    private List<Place> parseJsonToObjects(JsonObject json) {

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

        JsonElement jsonPlaces = null;
        try {
            jsonPlaces = ObjectBuildUtils.extractJsonElementFromJson(json, JSON_PATH);
        } catch (ObjectUtilsException respEx) {
            logger.error("Response from client error: {}", respEx.getMessage());
            throw new YqlClientException("Problem in the response: " + respEx.getMessage(), respEx);
        }

        List<Place> places = new LinkedList<>();
        if (jsonPlaces.isJsonArray()) {
            jsonPlaces.getAsJsonArray()
                    .forEach((jPlace) -> places.add(gson.fromJson(jPlace, Place.class)));
        } else {
            places.add(gson.fromJson(jsonPlaces, Place.class));
        }
        return places;

    }


}
