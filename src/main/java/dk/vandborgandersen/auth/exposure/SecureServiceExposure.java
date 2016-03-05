package dk.vandborgandersen.auth.exposure;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.core.SecurityContext;

import dk.vandborgandersen.auth.model.User;
import dk.vandborgandersen.auth.persistence.UserArchivist;

/**
 * Base class for service exposures that uses security.
 *
 * @author mortena@gmail.com
 */

public abstract class SecureServiceExposure {

    @Inject
    UserArchivist userArchivist;

    public String getUserIdentifier(SecurityContext securityContext) {
        return getUser(securityContext).gettId();
    }

    public User getUser(SecurityContext securityContext) {
        Optional<User> user = userArchivist.findUser(securityContext.getUserPrincipal().getName());
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new IllegalArgumentException("SecurityContext was not valid.");
        }
    }
}
