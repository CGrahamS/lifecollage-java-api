package com.krashidbuilt.api.resource;

import com.krashidbuilt.api.data.ApplicationUserData;
import com.krashidbuilt.api.model.ApplicationUser;
import com.krashidbuilt.api.model.Authentication;
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
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

@Path("private/user")
@Api(value = "private/user", description = "Interact with the user object", tags = "user")
public class ApplicationUserResource {
    private static Logger logger = LogManager.getLogger();

    //GET USER
    @GET()
    @Produces("application/json")
    @ApiOperation(value = "Get an existing user",
            notes = "Returns the user if it matches the request",
            response = ApplicationUser.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User not found", response = Error.class)
    })
    @Consumes("application/json")
    public Response getById(@Context HttpServletRequest servletRequest) {

        Authentication auth = (Authentication) servletRequest.getAttribute("Auth");
        logger.debug("Get user by id {} requested at user resource", auth.getUserId());

        ApplicationUser user = ApplicationUserData.getById(auth.getUserId());

        if (!user.isValid()) {
            logger.debug("CANNOT FIND USER");
            Error error = Error.notFound("USER", user.getId());
            return Response.status(error.getStatusCode()).entity(error).build();
        }

        //return user
        logger.debug("User {} found in the database and identified as {} {}.", user.getId(), user.getFirstName(), user.getLastName());
        return Response.ok(user).build();
    }

    //UPDATE USER
    @PUT()
    @Produces("application/json")
    @ApiOperation(value = "Update an existing users email",
        notes = "Updates the user's email if it matches the request",
        response = ApplicationUser.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User not found", response = Error.class),
            @ApiResponse(code = 403, message = "You're not permitted to update other users information", response = Error.class)
    })
    @Consumes("application/json")
    public Response update(ApplicationUser in, @Context UriInfo uriInfo, @Context HttpServletRequest servletRequest) {
        Authentication auth = (Authentication) servletRequest.getAttribute("Auth");
        logger.debug("Update email of user with id {} requested at user resource", auth.getUserId());
        ApplicationUser out;
        ApplicationUser user = ApplicationUserData.getById(in.getId());

        if (!user.isValid()) {
            logger.debug("CANNOT FIND USER");
            Error error = Error.notFound("USER", in.getId());
            return Response.status(error.getStatusCode()).entity(error).build();
        }

        if (user.getId() == auth.getUserId()) {
            out = ApplicationUserData.update(in);
        } else {
            logger.debug("CANNOT UPDATE OTHER USER");
            Error error = Error.forbidden();
            return Response.status(error.getStatusCode()).entity(error).build();
        }

        logger.debug("Updated email of user with id {}", auth.getUserId());
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        //return user
        return Response.created(builder.build()).entity(out).build();
    }

    //DELETE USER
    @DELETE()
    @Produces("application/json")
    @ApiOperation(value = "Delete an existing user",
        notes = "Deletes an existing user if it matches the request",
        response = ApplicationUser.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User not found", response = Error.class),
            @ApiResponse(code = 403, message = "You're not permitted to delete other users", response = Error.class)
    })
    @Consumes("application/json")
    public Response delete(@Context UriInfo uriInfo, @Context HttpServletRequest servletRequest) {

        Authentication auth = (Authentication) servletRequest.getAttribute("Auth");
        logger.debug("Delete user by id {} requested at user resource", auth.getUserId());

        ApplicationUser user = ApplicationUserData.getById(auth.getUserId());

        if (!user.isValid()) {
            logger.debug("CANNOT FIND USER");
            Error error = Error.notFound("USER", user.getId());
            return Response.status(error.getStatusCode()).entity(error).build();
        }

        if (user.getId() == auth.getUserId()) {
            ApplicationUserData.delete(auth.getUserId());
        } else {
            logger.debug("CANNOT DELETE OTHER USER");
            Error error = Error.forbidden();
            return Response.status(error.getStatusCode()).entity(error).build();
        }

        logger.debug("Deleted user with id {}", auth.getUserId());
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        return Response.ok(builder.build()).entity(user).build();
    }
}
