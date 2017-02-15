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
    @Path("user")
    @Produces("application/json")
    @ApiOperation(value = "Retrieve all collages that belong to a user or all collages",
            notes = "Return all collages that belong to the user with an id that matches the supplied id or return all collages",
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

        if (collages.size() <= 0) {
            return Response.ok(collages).build();
        }

        logger.debug("Collages that belong to user with id: {} found in the database", auth.getUserId());
        return Response.ok(collages).build();
    }

    @PUT()
    @Produces("application/json")
    @ApiOperation(value = "Update existing collage",
            notes = "Update existing collage with id that matches the supplied id",
            response = Collage.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "You are not allowed to update other users collages", response = Error.class),
            @ApiResponse(code = 404, message = "Collage not found", response = Error.class)
    })
    @Consumes("application/json")
    public Response updateCollage(Collage in, @Context UriInfo uriInfo, @Context HttpServletRequest servletRequest) {

        Authentication auth = (Authentication) servletRequest.getAttribute("Auth");
        logger.debug("Update collage with id {} title to {} requested by {} at collage resource", in.getId(), in.getTitle(), auth.getUserId());
        Collage collage = CollageData.getCollage(in.getId());
        Collage out;

        if (!collage.isValid()) {
            logger.debug("CANNOT FIND COLLAGE");
            Error error = Error.notFound("Collage", in.getId());
            return Response.status(error.getStatusCode()).entity(error).build();
        }

        if (collage.getUserId() == auth.getUserId()) {
            out = CollageData.updateCollageTitle(in);
        } else {
            logger.debug("CANNOT UPDATE COLLAGE USER DOES NOT OWN");
            Error error = Error.forbidden();
            return Response.status(error.getStatusCode()).entity(error).build();
        }

        logger.debug("Update title of single collage at update controller" + out.toString());
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        return Response.created(builder.build()).entity(out).build();
    }

    @DELETE()
    @Path("{collageId}")
    @Produces("application/json")
    @ApiOperation(value = "Delete collage",
        notes = "Delete collage with id that matches the supplied id"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "You are not allowed to delete other users collages", response = Error.class),
            @ApiResponse(code = 404, message = "Collage not found", response = Error.class)
    })
    @Consumes("application/json")
    public Response deleteCollage(@PathParam("collageId") int collageId,
                                  @Context UriInfo uriInfo,
                                  @Context HttpServletRequest servletRequest) {
        Authentication auth = (Authentication) servletRequest.getAttribute("Auth");
        logger.debug("Delete collage with id {} requested at collage resource", collageId);
        Collage collage = CollageData.getCollage(collageId);

        if (!collage.isValid()) {
            logger.debug("CANNOT FIND COLLAGE");
            Error error = Error.notFound("Collage", collage.getId());
            return Response.status(error.getStatusCode()).entity(error).build();
        }

        if (collage.getUserId() == auth.getUserId()) {
            CollageData.deleteCollage(collageId);
        } else {
            logger.debug("CANNOT DELETE COLLAGE USER DOES NOT OWN");
            Error error = Error.forbidden();
            return Response.status(error.getStatusCode()).entity(error).build();
        }

        logger.debug("Delete single collage at delete controller");
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        return Response.ok(builder.build()).entity(collage).build();
    }
}
