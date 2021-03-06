package com.krashidbuilt.api.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Ben Kauffman on 10/10/2016.
 * <p/>
 * A lightweight object that can be used to identify a user and their roles
 * The object will only be used by the API for authentication purposes
 * which will allow flexibility on user identification within the API
 */
public class Authentication implements Serializable {
    private ApplicationToken token;
    private int userId;
    private String name;
    private String email;

    public Authentication() {
        token = new ApplicationToken();
    }

    public static Authentication fromString(String json) {
        return new Gson().fromJson(json, Authentication.class);
    }

    public ApplicationToken getToken() {
        return token;
    }

    public void setToken(ApplicationToken token) {
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Authentication fromUser(ApplicationUser user) {
        this.setToken(user.getApplicationToken());
        this.setUserId(user.getId());
        this.setName(user.getFirstName() + " " + user.getLastName());
        this.setEmail(user.getEmail());
        return this;
    }

    public boolean isValid() {
        return userId >= 1;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
