package com.challenge.yql.api.weather.model;

import org.hibernate.validator.constraints.Email;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by springfield-home on 7/8/17.
 */
@Document
public class User {
    @Indexed(unique = true)
    private String username;
    private String password;
    @Email
    private String email;
    private boolean active;

    public User(){}

    public User(User user) {
        super();
        this.username = user.username;
        this.password = user.password;
        this.email = user.email;
        this.active = user.active;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
