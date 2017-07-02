package com.challenge.yql.client;

import com.challenge.yql.client.exception.YqlException;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

/**
 * Created by springfield-home on 7/1/17.
 */
@Service
public interface YqlClient {
    JsonObject query(YqlQuery query) throws YqlException;
}
