package com.challenge.yql.api.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.challenge")
public class WeatherChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherChallengeApplication.class, args);
    }
}
