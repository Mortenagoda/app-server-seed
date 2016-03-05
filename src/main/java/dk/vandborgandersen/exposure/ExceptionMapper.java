package dk.vandborgandersen.exposure;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

/**
 * Mapper for exceptions.
 *
 * @author mortena@gmail.com
 */

@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {
    private static final Logger LOGGER = Logger.getLogger(ExceptionMapper.class.getName());

    @Inject
    UriInfo uriInfo;

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof NotFoundException) {
            // URL was not found
            LOGGER.log(Level.INFO, "URL was not found: " + uriInfo.getAbsolutePath().toASCIIString());
            return Response.status(Response.Status.NOT_FOUND).build();
        } else if (exception instanceof ForbiddenException) {
            LOGGER.log(Level.FINE, "User tried to access something he was not allowed to.");
            return Response.status(Response.Status.FORBIDDEN).build();
        } else {
            LOGGER.log(Level.SEVERE, "Unhandled exception caught.", exception);
        }

        return Response.serverError().build();
    }
}
