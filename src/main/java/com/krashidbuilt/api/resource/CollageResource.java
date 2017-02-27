package com.cgrahams.api.resource;

import com.cgrahams.api.data.CollageData;
import com.cgrahams.api.data.PublicCollageData;
import com.cgrahams.api.model.Collage;
import com.cgrahams.api.model.Error;
import com.cgrahams.api.model.ThrowableError;
import com.cgrahams.api.model.Authentication;
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
        Collage collage = PublicCollageData.getCollage(in.getId());
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

    @PUT()
    @Path("updateOwner/{collageId}")
    @Produces("application/json")
    @ApiOperation(value = "Update existing collage",
            notes = "Update owner of existing collage with id that matches the supplied id",
            response = Collage.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Collage not found", response = Error.class)
    })
    @Consumes("application/json")
    public Response updateCollageOwner(@PathParam("collageId") int collageId, @Context UriInfo uriInfo, @Context HttpServletRequest servletRequest) {
        Authentication auth = (Authentication) servletRequest.getAttribute("Auth");

        logger.debug("Update owner of collage with id {} to owner with id {} requested at collage resource", collageId, auth.getUserId());

        Collage collage = PublicCollageData.getCollage(collageId);
        Collage out;

        if(!collage.isValid()) {
            logger.debug("CANNOT FIND COLLAGE");
            Error error = Error.notFound("Collage", collageId);
            return Response.status(error.getStatusCode()).entity(error).build();
        }

        out = CollageData.updateCollageOwner(collageId, auth.getUserId());

        logger.debug("Update owner of single collage at updateOwner controller");
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        return Response.ok(builder.build()).entity(out).build();
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
        Collage collage = PublicCollageData.getCollage(collageId);

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
