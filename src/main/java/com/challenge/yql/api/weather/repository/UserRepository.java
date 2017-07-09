package com.challenge.yql.api.weather.repository;

import com.challenge.yql.api.weather.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by springfield-home on 7/8/17.
 */
public interface UserRepository extends MongoRepository<User, String>{

    User findByUsername(String username);
}
