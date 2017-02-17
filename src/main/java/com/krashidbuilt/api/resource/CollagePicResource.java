package com.krashidbuilt.api.resource;

import com.krashidbuilt.api.data.CollagePicData;
import com.krashidbuilt.api.model.CollagePic;
import com.krashidbuilt.api.model.Error;
import com.krashidbuilt.api.model.ThrowableError;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

/**
 * Created by CGrahamS on 2/16/17.
 */

@Path("private/picture")
@Api(value= "private/picture", description = "Interact with the picture object", tags = "picture")
public class CollagePicResource {
    private static Logger logger = LogManager.getLogger();

    @POST()
    @Path("collageId/{collageId}")
    @Produces("application/json")
    @ApiOperation(value = "Create new picture",
    notes = "Returns the newly create collage if successfully created",
    response = CollagePic.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 422, message = "Picture creation failure", response = Error.class)
    })
    @Consumes("application/json")
    public Response createPicture(@PathParam("collageId") int collageId, CollagePic in,
                                  @Context UriInfo uriInfo) {
        logger.debug("Create picture with location of {} and collageId of {} at PrivateCollagePicResource", in.getLocation(), collageId);
        CollagePic out;

        try {
            out = CollagePicData.create(collageId, in);
        } catch (ThrowableError ex) {
            logger.debug("CAN'T CREATE PICTURE: {}", ex.getError().getDevMessage());
            Error error = ex.getError();
            return Response.status(error.getStatusCode()).entity(error).build();
        }

        // return newly created collagepic
        logger.debug("Create single picture: " + out.toString());
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        return Response.created(builder.build()).entity(out).build();
    }
}
