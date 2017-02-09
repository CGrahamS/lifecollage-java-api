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
}
