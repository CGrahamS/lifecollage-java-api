package com.krashidbuilt.api.resource;

import com.krashidbuilt.api.data.ApplicationUserData;
import com.krashidbuilt.api.data.CollageData;
import com.krashidbuilt.api.model.ApplicationUser;
import com.krashidbuilt.api.model.Collage;
import com.krashidbuilt.api.model.Error;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.List;

/**
 * Created by CGrahamS on 2/15/17.
 */

@Path("public/collage")
@Api(value = "public/collage", description = "Interact with a public collage object", tags = "collage")
public class PublicCollageResource {
    private static Logger logger = LogManager.getLogger();

    @GET()
    @Path("{collageId}")
    @Produces("application/json")
    @ApiOperation(value = "Retrieve the collage with the matching id",
            notes = "Return a collage that matches the specified id",
            response = Collage.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Collage not found", response = Error.class)
    })
    @Consumes("application/json")
    public Response getCollage(@PathParam("collageId") int collageId, @Context UriInfo uriInfo) {
        logger.debug("Get collage by id {} requested at collage resource", collageId);
        Collage collage = CollageData.getCollage(collageId);

        if (!collage.isValid()) {
            logger.debug("CANNOT FIND COLLAGE");
            Error error = Error.notFound("Collage", collageId);
            return Response.status(error.getStatusCode()).entity(error).build();
        }

        //return collage
        logger.debug("Collage with id:{} found in the database with the title:{}", collageId, collage.getTitle());
        return Response.ok(collage).build();
    }

    @GET()
    @Path("user/{userId}")
    @Produces("application/json")
    @ApiOperation(value = "Retrieve all collages that belong to a user",
            notes = "Return all collages that belong to the user with an id that matches the supplied id",
            response = Collage.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Unable to find user", response = Error.class)
    })
    @Consumes("application/json")
    public Response getCollages(@PathParam("userId") int userId) {
        ApplicationUser user = ApplicationUserData.getById(userId);
        List<HashMap<String, Object>> collages;

        if(!user.isValid()) {
            logger.debug("CANNOT FIND USER");
            Error error = Error.notFound("USER", userId);
            return Response.status(error.getStatusCode()).entity(error).build();
        } else {
            logger.debug("Get collages that belong to user id {} requested at collage resource", userId);
            collages = CollageData.getCollages(userId);
        }

        logger.debug("Collages that belong to user with id: {} found in the database", userId);
        return Response.ok(collages).build();
    }

    @GET()
    @Path("all")
    @Produces("application/json")
    @ApiOperation(value = "Retrieve all collages",
            notes = "Returns all existing collages",
            response = Collage.class
    )
    @Consumes("application/json")
    public Response getAllCollages(@Context UriInfo uriInfo, @Context HttpServletRequest servletRequest) {
        List<HashMap<String, Object>> collages;
        collages = CollageData.getCollages(0);

        logger.debug("Get collages that belong to all users");
        return Response.ok(collages).build();
    }
}
