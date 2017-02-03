package com.krashidbuilt.api.model;

import com.google.gson.Gson;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ben Kauffman on 1/15/2017.
 */

@ApiModel(value = "ApplicationUser", discriminator = "type")
public class ApplicationUser implements Serializable {

    private int id;
    @ApiModelProperty(value = "id", required = true)
    private String firstName;
    @ApiModelProperty(value = "firstName", required = true)
    private String lastName;
    @ApiModelProperty(value = "lastName", required = true)
    private String email;
    @ApiModelProperty(value = "email", required = true)

    private String password;
    @ApiModelProperty(value = "password", required = true)
    private String created;
    @ApiModelProperty(value = "created", required = false)
    private String updated;
    @ApiModelProperty(value = "updated", required = false)

    private ApplicationToken applicationToken;
    @ApiModelProperty(value = "applicationToken", required = false)

    private boolean valid;
    @ApiModelProperty(value = "valid", required = false)

    private List<HashMap> tags;
    private List<String> roles;


    public ApplicationUser() {
        tags = new ArrayList<>();
        applicationToken = new ApplicationToken();
        roles = new ArrayList<String>();
    }

    public static ApplicationUser fromString(String json) {
        return new Gson().fromJson(json, ApplicationUser.class);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
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

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public List<HashMap> getTags() {
        return tags;
    }

    public void setTags(List<HashMap> tags) {
        this.tags = tags;
    }

    public void setStringTags(List<String> stringTags) {
        tags = new ArrayList<>();
        for (String stringTag : stringTags) {
            HashMap tag = new HashMap();
            tag.put("tag", stringTag);
            tags.add(tag);
        }
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
