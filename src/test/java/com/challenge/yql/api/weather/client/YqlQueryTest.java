package com.challenge.yql.api.weather.client;

import com.challenge.yql.client.YqlQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Created by springfield-home on 7/3/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class YqlQueryTest {

    @MockBean
    private YqlQuery yqlQuery;

    @Before
    public void setup() throws URISyntaxException {
        given(yqlQuery.toUri())
                .willReturn(new URI("http://query.yahooapis.com/v1/public/yql?q=prueba"));
    }

    @Test
    public void testToUri() {
        assertThat(yqlQuery
                .toUri())
                .isEqualTo(new YqlQuery("prueba", 0).toUri());
    }
}
