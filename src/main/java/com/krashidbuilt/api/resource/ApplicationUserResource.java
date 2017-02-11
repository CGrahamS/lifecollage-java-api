package com.krashidbuilt.api.resource;

import com.krashidbuilt.api.data.ApplicationUserData;
import com.krashidbuilt.api.model.ApplicationUser;
import com.krashidbuilt.api.model.Authentication;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @Consumes("application/json")
    public Response getById(@Context HttpServletRequest servletRequest) {

        Authentication auth = (Authentication) servletRequest.getAttribute("Auth");
        logger.debug("Get user by id {} requested at user resource", auth.getUserId());

        ApplicationUser user = ApplicationUserData.getById(auth.getUserId());

        if (!user.isValid()) {
            return Response.status(404).build();
        }

        //return user
        logger.debug("User {} found in the database and identified as {} {}.", user.getId(), user.getFirstName(), user.getLastName());
        return Response.ok(user).build();
    }

    //UPDATE USER
    @PUT()
    @Produces("application/json")
    @ApiOperation(value = "Update an existing user",
        notes = "Updates the user if it matches the request",
        response = ApplicationUser.class
    )
    @Consumes("application/json")
    public Response update(ApplicationUser in, @Context UriInfo uriInfo, @Context HttpServletRequest servletRequest) {
        Authentication auth = (Authentication) servletRequest.getAttribute("Auth");
        logger.debug("Update user with id {} requested at user resource", auth.getUserId());
        ApplicationUser out;

        if (!in.isValid()) {
            return Response.status(404).build();
        }

        if (in.getId() == auth.getUserId()) {
            out = ApplicationUserData.update(in);
        } else {
            return Response.status(403).build();
        }

        logger.debug("Update username for user with id {}", auth.getUserId());
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        return Response.created(builder.build()).entity(out).build();
    }
}
