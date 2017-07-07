package com.challenge.yql.client.impl;

import com.challenge.yql.client.YqlClient;
import com.challenge.yql.client.YqlQuery;
import com.challenge.yql.client.exception.YqlException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * Created by springfield-home on 7/1/17.
 */
@Service
public class HttpYqlClient implements YqlClient {
    private final Logger logger = LoggerFactory
            .getLogger(HttpYqlClient.class);

    private CloseableHttpClient httpClient;

    public HttpYqlClient() {
        this.httpClient = HttpClients.createDefault();
    }

    @Override
    public JsonObject query(YqlQuery query) throws YqlException {
        Assert.notNull(query, "this argument is required; it must not be null");
        try {
            HttpUriRequest request = new HttpGet(query.toUri());
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                Integer statusCode = response.getStatusLine().getStatusCode();
                HttpEntity entity = response.getEntity();
                JsonObject jsonResult = new JsonParser().parse(EntityUtils.toString(entity)).getAsJsonObject();
                logger.debug("YQL query (URL={0}, Status Code={1})", query.toUri(), statusCode);
                if (statusCode == HttpStatus.SC_OK) {
                    return jsonResult;
                } else {
                    throw new YqlException("Failed to execute YQL query (URL=" +
                            query.toUri() + "): " + statusCode, jsonResult);
                }
            }
        } catch (ParseException | IOException e) {
            throw new YqlException("Failed to execute YQL query (URL="
                    + query.toUri() + "): " + e.getMessage(), e);
        }
    }
}
