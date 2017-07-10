package com.challenge.yql.api.weather.websocket;

import com.challenge.yql.api.weather.service.WeatherService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import sun.plugin.dom.exception.InvalidStateException;

import java.io.IOException;

/**
 * Created by springfield-home on 7/9/17.
 */
@Component
public class UpdateWeatherHandler implements WebSocketHandler {

    private final Gson gson = new GsonBuilder().create();

    private final WeatherService weatherService;


    @Autowired
    public UpdateWeatherHandler(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public void socketCallback(String json, WebSocketSession session) {
        if (!checkSession(session)) {
            throw new InvalidStateException("Session is not open yet or don't exist");
        }
        try {
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            throw new InvalidStateException("Could not send message to session: " + session.getId());
        }
    }

    private static boolean checkSession(WebSocketSession session) {
        return session != null && session.isOpen();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if (!checkSession(session)) {
            throw new InvalidStateException("Session is not open yet or don't exist");
        }
        String username = session.getPrincipal().getName();
        weatherService.addUserToUpdates(username);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (!checkSession(session)) {
            throw new InvalidStateException("Session is not open yet or don't exist");
        }
        try {
            String username = session.getPrincipal().getName();
            IncomingSocketMessage socketMessage = gson
                    .fromJson((String) (message.getPayload()), IncomingSocketMessage.class);
            weatherService.addWeatherToUpdate(socketMessage.woeid, username);
        } catch (JsonSyntaxException syntaxEx) {
            OutcommingSocketError error = new OutcommingSocketError(syntaxEx);
            socketCallback(gson.toJson(error), session);

        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String username = session.getPrincipal().getName();
        weatherService.removeWeatherToUpdate(username);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private static class IncomingSocketMessage {
        public Long woeid;
    }

    private static class OutcommingSocketError {
        public String error;
        public String message;

        public OutcommingSocketError(Exception syntaxEx) {
            this.error = "Error on message: ";
            this.message = syntaxEx.getMessage();
        }
    }
}
