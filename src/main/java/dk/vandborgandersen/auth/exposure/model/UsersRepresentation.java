package dk.vandborgandersen.auth.exposure.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import dk.vandborgandersen.auth.model.User;

/**
 * Representation of all users.
 *
 * @author mortena@gmail.com
 */
@XmlRootElement
public class UsersRepresentation {

    @NotNull
    private List<UserRepresentation> users;

    public UsersRepresentation() {

    }

    public UsersRepresentation(List<User> users) {
        this.users = new ArrayList<>();
        for (Iterator<User> iterator = users.iterator(); iterator.hasNext(); ) {
            User next = iterator.next();
            this.users.add(new UserRepresentation(next));
        }
    }

    public List<UserRepresentation> getUsers() {
        return users;
    }

    public void setUsers(List<UserRepresentation> users) {
        this.users = users;
    }
}
