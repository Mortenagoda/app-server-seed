package dk.vandborgandersen.auth.exposure;

import java.util.List;
import java.util.Optional;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import dk.vandborgandersen.auth.exposure.model.AccessTokenRepresentation;
import dk.vandborgandersen.auth.model.AccessToken;
import dk.vandborgandersen.auth.model.User;
import dk.vandborgandersen.auth.persistence.AccessTokenArchivist;
import dk.vandborgandersen.auth.persistence.UserArchivist;
import dk.vandborgandersen.auth.security.PasswordEncoder;
import dk.vandborgandersen.exposure.RepresentationHelper;

/**
 * Exposure handling login requests.
 *
 * @author mortena@gmail.com
 */

@Path("/auth")
@PermitAll
public class LoginExposure {

    @Inject
    RepresentationFactory representationFactory;

    @Inject
    RepresentationHelper representationHelper;

    @Inject
    UserArchivist userArchivist;

    @Inject
    PasswordEncoder passwordEncoder;

    @Inject
    AccessTokenArchivist accessTokenArchivist;

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/hal+json")
    public Response login(@FormParam("email") String email, @FormParam("password") String password, @Context UriInfo uriInfo) {
        Optional<User> user = userArchivist.findUser(email);
        if (user.isPresent()) {
            if (passwordEncoder.matches(password, user.get().getChallengeEncrypted())) {
                AccessToken token = accessTokenArchivist.createToken(user.get());

                Representation entity =
                    representationFactory.newRepresentation(uriInfo.getBaseUriBuilder().path(LoginExposure.class).build());
                entity.withBean(new AccessTokenRepresentation(token));
                return Response.ok(entity).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(representationHelper.createError(1, "Credentials not correct.", uriInfo))
            .build();
    }

    @GET
    @Path("accessTokens")
    @RolesAllowed("USER_ADMIN")
    public Response list(@Context UriInfo uriInfo) {
        List<AccessToken> list = accessTokenArchivist.list();

        Representation representation = representationFactory
            .newRepresentation(uriInfo.getBaseUriBuilder().path(LoginExposure.class).path(LoginExposure.class, "list").build());
        list.stream().forEach(token -> representation
            .withRepresentation("accessTokens", representationFactory.newRepresentation().withBean(new AccessTokenRepresentation(token))));

        return Response.ok(representation).build();
    }
}
