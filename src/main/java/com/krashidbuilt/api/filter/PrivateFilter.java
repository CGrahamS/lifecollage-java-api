package com.cgrahams.api.filter;

import com.cgrahams.api.data.AuthenticationData;
import com.cgrahams.api.helpers.DateTime;
import com.cgrahams.api.helpers.ErrorResponse;
import com.cgrahams.api.model.Authentication;
import com.cgrahams.api.model.ThrowableError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Ben Kauffman on 1/15/2017.
 */
public class PrivateFilter implements Filter {

    private static Logger logger = LogManager.getLogger();
    private FilterConfig filterConfig;

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    @Override
    public void init(FilterConfig filterConfig2) throws ServletException {
        setFilterConfig(filterConfig2);
    }

    @Override
    public void destroy() {

        logger.debug("Destroy private filter : " + ((filterConfig != null) ? filterConfig.toString() : null));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        logger.debug("WE ARE HERE");

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        Authentication auth = new Authentication();

        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader != null) {
            String[] bearer = authHeader.split("Bearer ");
            if (bearer.length == 2) {
                try {
                    auth = AuthenticationData.parseToken(bearer[1], AuthenticationData.TokenType.ACCESS);
                } catch (Exception ex) {
                    logger.error("JWT TOKEN IS NOT VALID OR IS EXPIRED", ex);
                } catch (ThrowableError throwableError) {
                    logger.error(throwableError.getError().getDevMessage(), throwableError);
                }
            }

        }

        if (!auth.isValid()) {
            // is not a valid user
            ErrorResponse.unauthorized(servletResponse);
            return;
        }

        String uuid = "[" + UUID.randomUUID().toString() + "]";
        logger.debug(uuid + " Private request made by " + auth.getName() + " started " + DateTime.nowToString());

        servletRequest.setAttribute("Auth", auth);

        filterChain.doFilter(servletRequest, servletResponse);

        logger.debug(uuid + " Private request made by " + auth.getName() + " completed " + DateTime.nowToString());
    }

}
