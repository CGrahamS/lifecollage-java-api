package com.cgrahams.api.model;

import com.google.gson.Gson;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 * Created by Ben Kauffman on 1/15/2017.
 */

@ApiModel(value = "PublicApplicationUser", discriminator = "type")
public class PublicApplicationUser implements Serializable {

    private int id;
    private String firstName;
    private String lastName;
    private String username;


    private ApplicationToken applicationToken;


    public PublicApplicationUser() {
        applicationToken = new ApplicationToken();
    }

    public static PublicApplicationUser fromString(String json) {
        return new Gson().fromJson(json, PublicApplicationUser.class);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ApplicationToken getApplicationToken() {
        return applicationToken;
    }

    public void setApplicationToken(ApplicationToken applicationToken) {
        this.applicationToken = applicationToken;
    }

    public boolean isValid() {
        return id >= 1;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
