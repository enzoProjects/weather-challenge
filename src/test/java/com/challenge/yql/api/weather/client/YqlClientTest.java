package com.challenge.yql.api.weather.client;

import com.challenge.yql.client.YqlClient;
import com.challenge.yql.client.YqlQuery;
import com.challenge.yql.client.exception.YqlException;
import com.challenge.yql.client.impl.HttpYqlClient;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Created by springfield-home on 7/3/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class YqlClientTest {

    private static final String TEST_URL = "https://query.yahooapis.com/v1/public/yql?" +
            "q=select%20*%20from%20weather.forecast%20where%20woeid%3D2502265&" +
            "format=json";
    @MockBean
    private YqlClient yqlClient;
    @MockBean
    private YqlQuery yqlQuery;
    private JsonObject jsonObject;


    @Before
    public void setup() throws FileNotFoundException, URISyntaxException {
        FileReader fileReader = new FileReader(getClass()
                .getResource("/jsonQueryResult.json")
                .getFile());
        jsonObject = new JsonParser()
                .parse(fileReader)
                .getAsJsonObject();
        given(yqlQuery
                .toUri())
                .willReturn(new URI(TEST_URL));


    }

    @Test
    public void testQuery() throws YqlException {
        given(yqlClient.query(yqlQuery))
                .willReturn(jsonObject);
        assertThat(yqlClient
                .query(yqlQuery))
                .isEqualTo(new HttpYqlClient().query(yqlQuery));
    }
}
