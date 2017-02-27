package com.krashidbuilt.api.model;

import com.google.gson.Gson;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 * Created by CGrahamS on 2/16/17.
 */

@ApiModel(value = "CollagePic", discriminator = "type")
public class CollagePic implements Serializable {

    private int id;
    private int collageId;
    private String location;
    private String created;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCollageId() {
        return collageId;
    }

    public void setCollageId(int collageId) {
        this.collageId = collageId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public boolean isValid() {
        return id >= 1;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
