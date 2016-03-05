package dk.vandborgandersen.auth.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import dk.vandborgandersen.auth.model.AccessToken;
import dk.vandborgandersen.auth.model.User;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Archivist handling issued tokens.
 *
 * @author mortena@gmail.com
 */

public class AccessTokenArchivist {
    private static final long EXPIRY_TIME_MS = 1000 * 60 * 10;
    private static final String ACCESS_TOKEN = "AccessTokens";

    @Inject
    private MongoOperations mongoOperations;

    public Optional<AccessToken> find(String accessToken) {
        List<AccessToken> found =
            mongoOperations.find(new Query(Criteria.where("accessToken").is(accessToken)), AccessToken.class, ACCESS_TOKEN);
        if (found == null || found.isEmpty()) {
            return Optional.empty();
        }
        if (found.size() > 1) {
            throw new RuntimeException("Multiple instances of the accessToken.");
        } else {
            return Optional.of(found.get(0));
        }
    }

    public List<AccessToken> list() {
        return mongoOperations.findAll(AccessToken.class, ACCESS_TOKEN);
    }

    public AccessToken createToken(User user) {
        int maxTries = 10;
        String token = null;
        AccessToken accessToken = null;
        boolean doDelete = false;
        while (accessToken == null && maxTries-- > 0) {
            // Try with new token
            token = UUID.randomUUID().toString();
            accessToken = new AccessToken(user.gettId(), token, EXPIRY_TIME_MS, System.currentTimeMillis());
            mongoOperations.insert(accessToken, ACCESS_TOKEN);
            try {
                Optional<AccessToken> existingToken = find(token);
                if (!(existingToken.isPresent() && existingToken.get().gettId().equals(accessToken.gettId()))) {
                    doDelete = true;
                }
            } catch (Exception e) {
                // Token was probably a duplicate, delete it again.
                doDelete = true;
            }
            if (doDelete) {
                mongoOperations.remove(accessToken);
                accessToken = null;
            }
        }
        if (accessToken != null) {
            return accessToken;
        } else {
            throw new RuntimeException("Unable to generate and persist new access token.");
        }
    }
}
