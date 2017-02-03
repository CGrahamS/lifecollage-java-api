package com.krashidbuilt.api.data;

import com.krashidbuilt.api.model.ApplicationUser;
import com.krashidbuilt.api.model.ThrowableError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Ben Kauffman on 1/15/2017.
 */
public final class ApplicationUserData {

    private static Logger logger = LogManager.getLogger();

    public static ApplicationUser create(ApplicationUser in) throws ThrowableError {
        logger.debug("CREATE USER");
        return in;
    }

    public static ApplicationUser getById(int userId) {
        logger.debug("GET USER {} BY ID", userId);
        return new ApplicationUser();
    }

    public static ApplicationUser getByEmail(String email){
        logger.debug("GET USER {} BY EMAIL", email);
        return new ApplicationUser();
    }

}