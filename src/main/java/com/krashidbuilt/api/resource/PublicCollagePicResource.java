package com.krashidbuilt.api.resource;

import com.krashidbuilt.api.data.CollagePicData;
import com.krashidbuilt.api.data.PublicCollageData;
import com.krashidbuilt.api.data.PublicCollagePicData;
import com.krashidbuilt.api.model.Collage;
import com.krashidbuilt.api.model.CollagePic;
import com.krashidbuilt.api.model.Error;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by CGrahamS on 2/17/17.
 */

@Path("public/picture")
@Api(value = "public/picture", description = "Interact with a public picture object", tags = "picture")
public class PublicCollagePicResource {
    private static Logger logger = LogManager.getLogger();

    @GET()
    @Path("all/collageId/{collageId}")
    @Produces("application/json")
    @ApiOperation(value = "Retrieve all pictures associated with collage",
            notes = "Returns all pictures associated with the collage that matches the supplied id",
            response = CollagePic.class,
            responseContainer = "List"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Collage not found", response = Error.class)
    })
    @Consumes("application/json")
    public Response getCollagePictures(@PathParam("collageId") int collageId) {
        logger.debug("Find most recent picture of collage with id {} at PublicCollagePicResource", collageId);

        Collage collage = PublicCollageData.getCollage(collageId);
        List<CollagePic> out;

        if (!collage.isValid()) {
            logger.debug("CANNOT FIND COLLAGE");
            Error error = Error.notFound("Collage", collageId);
            return Response.status(error.getStatusCode()).entity(error).build();
        }

        out = PublicCollagePicData.getCollagePics(collageId);

        // return most recent picture of collage with supplied id
        logger.debug("Get single picture" + out.toString());
        return Response.ok(out).build();
    }

    @GET()
    @Path("collageId/{collageId}")
    @Produces("application/json")
    @ApiOperation(value = "Retrieve most recent picture of collage",
    notes = "Returns most recent picture of the collage with the supplied id",
    response = CollagePic.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Collage not found", response = Error.class)
    })
    @Consumes("application/json")
    public Response getRecentPicture(@PathParam("collageId") int collageId) {
        logger.debug("Find most recent picture of collage with id {} at PublicCollagePicResource", collageId);

        Collage collage = PublicCollageData.getCollage(collageId);
        CollagePic out;

        if (!collage.isValid()) {
            logger.debug("CANNOT FIND COLLAGE");
            Error error = Error.notFound("Collage", collageId);
            return Response.status(error.getStatusCode()).entity(error).build();
        }

        out = CollagePicData.getRecentPic(collageId);

        // return most recent picture of collage with supplied id
        logger.debug("Get single picture" + out.toString());
        return Response.ok(out).build();
    }
}
