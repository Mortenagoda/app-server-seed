package dk.vandborgandersen.auth.exposure;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import dk.vandborgandersen.auth.exposure.model.AddUserRepresentation;
import dk.vandborgandersen.auth.exposure.model.UserRepresentation;
import dk.vandborgandersen.auth.exposure.model.UsersRepresentation;
import dk.vandborgandersen.auth.model.User;
import dk.vandborgandersen.auth.persistence.UserArchivist;
import dk.vandborgandersen.env.TransactionLogger;
import dk.vandborgandersen.exposure.RepresentationHelper;

/**
 * REST exposure for Users.
 *
 * @author mortena@gmail.com
 */
@Path("/users")
@PermitAll
public class UserServiceExposure {
    private static final Logger LOGGER = Logger.getLogger(UserServiceExposure.class.getName());

    @Inject
    RepresentationFactory representationFactory;

    @Inject
    RepresentationHelper representationHelper;

    @Inject
    UserArchivist userArchivist;

    @Inject
    TransactionLogger transactionLogger;

    @GET
    @RolesAllowed("USER_ADMIN")
    @Produces({ "application/hal+json" })
    public Response listUsers(@Context UriInfo uriInfo) {
        final Representation representation = representationFactory.newRepresentation(
            UriBuilder.fromUri(uriInfo.getBaseUri()).path(UserServiceExposure.class).build());
        List<User> users = userArchivist.listUsers();
        UsersRepresentation usersRepresentation = new UsersRepresentation(users);
        representation.withBean(usersRepresentation);
        return Response.ok(representation).build();
    }

    @GET
    @Path("{email}")
    @Produces({ "application/hal+json" })
    public Response get(@Context UriInfo uriInfo, @PathParam("email") String email) {
        transactionLogger.fine("GET user by email: " + email);
        Optional<User> user = userArchivist.findUser(email.toLowerCase());
        if (user.isPresent()) {
            return Response.ok().entity(new UserRepresentation(user.get())).build();
        }
        return Response.noContent().build();
    }

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/hal+json" })
    public Response create(@Context UriInfo uriInfo, @Valid AddUserRepresentation newUser) {
        LOGGER.finest("Received user: " + newUser.toString());
        transactionLogger.info("Created new user: " + newUser.toString());
        try {
            User user = new User(newUser.getEmail().toLowerCase(), newUser.getPassword());
            if (userArchivist.findUser(newUser.getEmail().toLowerCase()).isPresent()) {
                return Response.status(Response.Status.BAD_REQUEST).entity(
                    representationHelper.createError(1, "Invalid input.", uriInfo)).build();
            }
            userArchivist.add(user);
            URI uri = UriBuilder.fromUri(uriInfo.getBaseUri()).path(UserServiceExposure.class)
                .path(UserServiceExposure.class, "get")
                .build(user.getEmail().toLowerCase());
            return Response.created(uri).build();
        } catch (Throwable e) {
            LOGGER.log(Level.SEVERE, "Unexpected exception caught creating user.", e);
            transactionLogger.severe("Creating user failed: " + newUser.toString());
            return Response.serverError().build();
        }
    }
}
