package com.challenge.yql.api.weather.websocket;

import com.challenge.yql.api.weather.service.impl.WeatherServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import sun.plugin.dom.exception.InvalidStateException;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by springfield-home on 7/9/17.
 */
@Component
public class UpdateWeatherHandler implements WebSocketHandler {

    Logger logger = LoggerFactory.getLogger(UpdateWeatherHandler.class);


    private final Gson gson = new GsonBuilder().create();


    private static final Map<String, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();


    public UpdateWeatherHandler() {
    }

    public void sendWeatherUpdate(Long woeid, Set<String> usernames) {
        if (usernames == null) return;
        logger.debug("we gonna send you updates this usernames : " + (usernames) + " adding this woeid to the list: " + woeid);
        usernames.forEach((u)-> prepareUpdate(u, woeid));
    }



    public static boolean isActiveUser(String username) {
        return SESSIONS.containsKey(username);
    }

    private void socketCallback(String json, WebSocketSession session) {
        if (!checkSession(session)) {
            throw new InvalidStateException("Session is not open yet or don't exist");
        }
        try {
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            throw new InvalidStateException("Could not send message to session: " + session.getId());
        }
    }



    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if (!checkSession(session)) {
            throw new InvalidStateException("Session is not open yet or don't exist");
        }
        //String username = session.getPrincipal().getName();
        String username = "prueba";
        logger.info("new websocket connection: " + username);
        SESSIONS.put(username, session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (!checkSession(session)) {
            throw new InvalidStateException("Session is not open yet or don't exist");
        }
        try {
            //String username = session.getPrincipal().getName();
            String username = "prueba";
            IncomingSocketMessage socketMessage = gson
                    .fromJson((String) (message.getPayload()), IncomingSocketMessage.class);
            switch (socketMessage.action) {
                case "add":
                    logger.debug("new weather update ask from: " + username + " adding this woeid to the list: " + socketMessage.woeid);
                    WeatherServiceImpl.addWeatherToUpdate(socketMessage.woeid, username);
                    break;
                case "remove":
                    logger.debug("remove weather update ask from: " + username + " removing this woeid to the list: " + socketMessage.woeid);
                    WeatherServiceImpl.removeWeatherToUpdate(socketMessage.woeid, username);
                    break;
            }
        } catch (JsonSyntaxException syntaxEx) {
            OutSocketError error = new OutSocketError(syntaxEx);
            socketCallback(gson.toJson(error), session);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        //String username = session.getPrincipal().getName();
        String username = "prueba";
        logger.info("remove websocket connection: " + username);
        SESSIONS.remove(username);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private void prepareUpdate(String username, Long woeid) {
        WebSocketSession session = SESSIONS.get(username);
        if(!checkSession(session)) {
            throw new InvalidStateException("Session is not open yet or don't exist");
        }
        try {
            IncomingSocketMessage socketMessage = new IncomingSocketMessage();
            socketMessage.woeid = woeid;
            socketCallback(gson.toJson(socketMessage), session);
        } catch (Exception parseError) {
            OutSocketError error = new OutSocketError(parseError);
            socketCallback(gson.toJson(error), session);
        }
    }

    private static class IncomingSocketMessage {
        public String action;
        public Long woeid;
    }

    private static boolean checkSession(WebSocketSession session) {
        return session != null && session.isOpen();
    }

    private static class OutSocketError {
        public String error;
        public String message;

        public OutSocketError(Exception syntaxEx) {
            this.error = "Error on message: ";
            this.message = syntaxEx.getMessage();
        }
    }
}
