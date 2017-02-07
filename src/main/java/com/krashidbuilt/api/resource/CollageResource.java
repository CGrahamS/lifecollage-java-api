package com.krashidbuilt.api.resource;

import com.krashidbuilt.api.data.CollageData;
import com.krashidbuilt.api.model.Authentication;
import com.krashidbuilt.api.model.Collage;
import com.krashidbuilt.api.model.Error;
import com.krashidbuilt.api.model.ThrowableError;
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
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.List;

/**
 * Created by CGrahamS on 2/3/17.
 */

@Path("private/collage")
@Api(value = "private/collage", description = "Interact with the collage object", tags = "collage")
public class CollageResource {
    private static Logger logger = LogManager.getLogger();

    @POST()
    @Path("create")
    @Produces("application/json")
    @ApiOperation(value = "Create new collage",
            notes = "Returns the newly create collage if successfully created",
            response = Collage.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 422, message = "Collage creation failure", response = Error.class)
    })
    @Consumes("application/json")
    public Response createCollage(Collage in,
                                  @Context UriInfo uriInfo,
                                  @Context HttpServletRequest servletRequest) {
        Authentication auth = (Authentication) servletRequest.getAttribute("Auth");

        Collage out;

        try {
            out = CollageData.create(auth.getUserId(), in);
        } catch (ThrowableError ex) {
            logger.debug("CAN'T CREATE COLLAGE {} {}", in.getTitle(), ex.getError().getDevMessage());
            Error error = ex.getError();
            return Response.status(error.getStatusCode()).entity(error).build();
        }

        //return newly created collage
        logger.debug("Create single collage" + out.toString());
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        return Response.created(builder.build()).entity(out).build();
    }

    @GET()
    @Path("{collageId}")
    @Produces("application/json")
    @ApiOperation(value = "Retrieve the collage with the matching id",
            notes = "Return a collage that matches the specified id",
            response = Collage.class
    )
    @Consumes("application/json")
    public Response getCollage(@PathParam("collageId") int collageId, @Context UriInfo uriInfo) {
        logger.debug("Get collage by id {} requested at collage resource", collageId);
        Collage collage = CollageData.getCollage(collageId);

        if (!collage.isValid()) {
            return Response.status(404).build();
        }

        //return collage
        logger.debug("Collage with id:{} found in the database with the title:{}", collageId, collage.getTitle());
        return Response.ok(collage).build();
    }

    @GET()
    @Path("user")
    @Produces("application/json")
    @ApiOperation(value = "Retrieve all collages that belong to a user",
            notes = "Return all collages that belong to the user with an id that matches the supplied id",
            response = Collage.class
    )
    @Consumes("application/json")
    public Response getCollages(
            @QueryParam("all") boolean all,
            @Context HttpServletRequest servletRequest) {
        Authentication auth = (Authentication) servletRequest.getAttribute("Auth");

        List<HashMap<String, Object>> collages;
        if (all) {
            logger.debug("Get collages that belong to all users, request by {}", auth.getName());
            collages = CollageData.getCollages(0);
        } else {
            logger.debug("Get collages that belong to user id {} requested at collage resource", auth.getUserId());
            collages = CollageData.getCollages(auth.getUserId());
        }

        logger.debug("Collages that belong to user with id: {} found in the database", auth.getUserId());
        return Response.ok(collages).build();
    }

    @PUT()
    @Path("update/{collageId}")
    @Produces("application/json")
    @ApiOperation(value = "Update existing collage",
            notes = "Update existing collage with id that matches the supplied id",
            response = Collage.class
    )
    @Consumes("application/json")
    public Response updateCollage(@PathParam("collageId") int collageId, @PathParam("collageTitle") String collageTitle, @Context UriInfo uriInfo) {
        logger.debug("Update collage with id {} requested at collage resource", collageId);
        Collage collage = CollageData.updateCollage(collageId, collageTitle);

        if (!collage.isValid()) {
            return Response.status(404).build();
        }

        logger.debug("Collage with id {} updated", collageId);
        return Response.ok(collage).build();
    }
}
