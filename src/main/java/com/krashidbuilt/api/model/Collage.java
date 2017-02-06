package com.krashidbuilt.api.model;

import com.google.gson.Gson;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 * Created by CGrahamS on 2/3/17.
 */
@ApiModel(value = "Collage", discriminator = "type")
public class Collage implements Serializable {

    private int id;
    private String title;
    private String created;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
