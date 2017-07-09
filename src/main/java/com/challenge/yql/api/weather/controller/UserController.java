package com.challenge.yql.api.weather.controller;

import com.challenge.yql.api.weather.model.User;
import com.challenge.yql.api.weather.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by springfield-home on 7/9/17.
 */
@RestController
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST )
    public void createNewUser(@Valid @RequestBody User user) {
        userService.createUser(user);
    }
}
