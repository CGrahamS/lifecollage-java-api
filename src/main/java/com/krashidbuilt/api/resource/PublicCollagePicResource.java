package com.krashidbuilt.api.resource;

import com.krashidbuilt.api.data.CollagePicData;
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

/**
 * Created by CGrahamS on 2/17/17.
 */

@Path("public/picture")
@Api(value = "public/picture", description = "Interact with a public picture object", tags = "picture")
public class PublicCollagePicResource {
    private static Logger logger = LogManager.getLogger();

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

        CollagePic picture;

        picture = CollagePicData.getRecentPic(collageId);

        // return most recent picture of collage with supplied id
        logger.debug("Get single picture" + picture.toString());
        return Response.ok(picture).build();
    }
}
