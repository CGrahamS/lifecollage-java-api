package com.krashidbuilt.api.resource;

import com.krashidbuilt.api.data.ApplicationUserData;
import com.krashidbuilt.api.model.PublicApplicationUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by CGrahamS on 2/16/17.
 */

@Path("public/user")
@Api(value = "public/user", description = "Interact with a public user object", tags = "user")
public class PublicApplicationUserResource {
    private static Logger logger = LogManager.getLogger();

    //GET USER BY USERNAME
    @GET()
    @Produces("application/json")
    @ApiOperation(value = "Get existing users",
            notes = "Returns users whose username matches a query",
            response = PublicApplicationUser.class,
            responseContainer = "List"
    )
    @Consumes("application/json")
    public Response getByUsername(@QueryParam("username") String searchQuery) {
        logger.debug("Get users whose user name matches search query: {}", searchQuery);

        List<PublicApplicationUser> users;

        users = ApplicationUserData.getByUsername(searchQuery);

        logger.debug("Users with username matching query string {} found in database.", searchQuery);
        return Response.ok(users).build();
    }
}
