package dk.vandborgandersen.auth.security;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import dk.vandborgandersen.auth.model.AccessToken;
import dk.vandborgandersen.auth.model.User;
import dk.vandborgandersen.auth.persistence.AccessTokenArchivist;
import dk.vandborgandersen.auth.persistence.UserArchivist;

/**
 * Authorization filter. Validates the Bearer token and eventually instantiates the security context for later use.
 *
 * @author mortena@gmail.com
 */
@Provider
@PreMatching
public class AuthFilter implements ContainerRequestFilter {
    @Inject
    AccessTokenArchivist accessTokenArchivist;

    @Inject
    UserArchivist userArchivist;

    @Override
    public void filter(ContainerRequestContext requestContext)
        throws IOException {
        String principal = "anonymous";
        Map<String, Boolean> rolesMap = new HashMap<String, Boolean>();
        String tokenHeader = requestContext.getHeaders().getFirst("X-Auth-Token");
        String bearerToken = null;
        if (tokenHeader != null && tokenHeader.contains("Bearer")) {
            String[] split = tokenHeader.split(" ");
            if (split.length > 1 && split[1].length() > 0) {
                bearerToken = split[1];
            }
        }
        if (bearerToken != null) {
            Optional<AccessToken> accessToken = accessTokenArchivist.find(bearerToken);
            if (accessToken.isPresent() &&
                accessToken.get().getIssueTimestamp() + accessToken.get().getExpiryTime() > System.currentTimeMillis()) {
                // Token is okay
                Optional<User> user = userArchivist.findByTId(accessToken.get().getUserTId());
                if (user.isPresent()) {
                    principal = user.get().getEmail();
                    if (user.get().getEmail().equalsIgnoreCase("admin@vandborgandersen.dk")) {
                        rolesMap.put("USER_ADMIN", Boolean.TRUE);
                    }
                }
            }
        }
        final String principalName = principal;

        final Principal principalObj = new Principal() {
            @Override
            public String getName() {
                return principalName;
            }
        };
        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return principalObj;
            }

            @Override
            public boolean isUserInRole(String s) {
                if (rolesMap.containsKey(s)) {
                    return rolesMap.get(s);
                }
                return false;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public String getAuthenticationScheme() {
                return "Bearer";
            }
        });
    }
}