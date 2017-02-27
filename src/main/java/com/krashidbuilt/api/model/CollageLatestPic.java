package com.cgrahams.api.model;

import com.google.gson.Gson;
import io.swagger.annotations.ApiModel;

/**
 * Created by CGrahamS on 2/20/17.
 */

@ApiModel(value = "Collage", discriminator = "type")
public class CollageLatestPic {

    private Collage collage;
    private CollagePic collagePic;

    public Collage getCollage() {
        return collage;
    }

    public void setCollage(Collage collage) {
        this.collage = collage;
    }

    public CollagePic getCollagePic() {
        return collagePic;
    }

    public void setCollagePic(CollagePic collagePic) {
        this.collagePic = collagePic;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
