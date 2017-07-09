package com.challenge.yql.api.weather.service;

import com.challenge.yql.api.weather.model.User;
import com.challenge.yql.api.weather.service.exception.UserCreationException;
import org.springframework.stereotype.Service;

/**
 * Created by springfield-home on 7/9/17.
 */
@Service
public interface UserService {
    void createUser(User user) throws UserCreationException;
}
