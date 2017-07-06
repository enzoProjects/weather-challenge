package com.challenge.yql.api.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = RepositoryRestMvcAutoConfiguration.class)
@EnableScheduling
@ComponentScan(basePackages = "com.challenge")
public class WeatherChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherChallengeApplication.class, args);
    }
}
