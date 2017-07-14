package com.challenge.yql.api.weather.service.impl;

import com.challenge.yql.api.weather.model.User;
import com.challenge.yql.api.weather.repository.UserRepository;
import com.challenge.yql.api.weather.service.UserService;
import com.challenge.yql.api.weather.service.exception.UserCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * Created by springfield-home on 7/9/17.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(User user) throws UserCreationException {
        try {
            user.setActive(true);
            userRepository.insert(user);
        } catch (DuplicateKeyException ex) {
            throw new UserCreationException("the user exists: " + user.getUsername(), ex);
        }
    }

    @Override
    public void saveToken(String uuid, Object principal) {

    }
}
