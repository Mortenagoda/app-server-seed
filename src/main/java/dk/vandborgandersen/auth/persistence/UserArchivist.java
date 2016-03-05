package dk.vandborgandersen.auth.persistence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.NoResultException;

import dk.vandborgandersen.auth.model.User;
import dk.vandborgandersen.auth.security.PasswordEncoder;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * User archivist used for persisting users.
 *
 * @author mortena@gmail.com
 */

@Singleton
public class UserArchivist {
    private static final Logger LOGGER = Logger.getLogger(UserArchivist.class.getName());
    private static final String USERS_COL = "Users";
    private boolean adminIsChecked;
    @Inject
    private MongoOperations mongoOperations;

    @Inject
    private PasswordEncoder passwordEncoder;

    public UserArchivist() {

    }

    public void add(User user) {
        user.settId(UUID.randomUUID().toString());
        mongoOperations.insert(user, USERS_COL);
    }

    public List<User> listUsers() {
        List<User> resultList = mongoOperations.findAll(User.class, USERS_COL);
        return resultList;
    }

    public Optional<User> findUser(String email) {
        try {
            ensureAdminUser();
            return Optional.of(getUser(email));
        } catch (NoResultException noResException) {
            return Optional.empty();
        }
    }

    public Optional<User> findByTId(String tId) {
        try {
            List<User> users = mongoOperations.find(new Query(Criteria.where("tId").is(tId)), User.class, USERS_COL);
            if (users != null && users.size() == 1) {
                return Optional.of(users.get(0));
            } else if (users.size() > 1) {
                throw new RuntimeException("Multiple users.");
            } else {
                return Optional.empty();
            }
        } catch (NoResultException no) {
            return Optional.empty();
        }
    }

    private User getUser(String email) {
        List<User> usersForEmail = mongoOperations.find(new Query(Criteria.where("email").is(email)), User.class, USERS_COL);
        if (usersForEmail.size() == 1) {
            return usersForEmail.get(0);
        }
        return null;
    }

    private void ensureAdminUser() {
        if (!adminIsChecked) {
            // Server starting
            String adminUsername = "admin@vandborgandersen.dk";
            User user = null;
            try {
                user = getUser(adminUsername);
            } catch (Exception e) {
                // Just ignore
            }
            if (user == null) {
                String adminPassword = "password"; // UUID.randomUUID().toString();
                add(new User(adminUsername, passwordEncoder.encode(adminPassword)));
                LOGGER.info("Added admin user with password: " + adminPassword);
            }
            adminIsChecked = true;
        }
    }

    public List<String> getEmailsForUserTIds(List<String> relationUserTIds) {
        List<String> emails = new ArrayList<>();
        List<User> users = mongoOperations.find(new Query(Criteria.where("tId").in(relationUserTIds)), User.class, USERS_COL);
        for (Iterator<User> iterator = users.iterator(); iterator.hasNext(); ) {
            emails.add(iterator.next().getEmail());
        }
        return emails;
    }
}
