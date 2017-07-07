package com.challenge.yql.api.weather.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Created by springfield-home on 7/1/17.
 */
@Document
public class Weather {
    @Id
    public String id;
    public Long woeid;
    public Forecast item;

    public class Forecast {
        public List<ForecastItem> forecast;
        public Date pubDate;

        public class ForecastItem {
            public Integer code;
            public Integer low;
            public Integer high;
            public String day;
            public String text;
            public String date;
        }
    }


}