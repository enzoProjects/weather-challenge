package com.challenge.yql.api.weather.model;

/**
 * Created by springfield-home on 7/1/17.
 */
public class Place {
    public Long woeid;
    public Location country;
    public Location admin1;
    public Location admin2;
    public Location admin3;
    public Location locality1;
    public Location locality2;

    public boolean isbCountry() {
        return admin1 == null;
    }

    public String getPrettyName() {
        StringBuilder sb = new StringBuilder();
        sb.append(country.content);
        if (isbCountry()) return sb.toString();
        return formNameFromLocations(sb, admin1, admin2, admin3, locality1, locality2);
    }

    private String formNameFromLocations(StringBuilder sb, Location... locations) {
        for (Location location : locations) {
            if (location != null) sb.append(", ").append(location.content);
        }
        return sb.toString();
    }

    public class Location {
        public String type;
        public String woeid;
        public String content;
    }
}


