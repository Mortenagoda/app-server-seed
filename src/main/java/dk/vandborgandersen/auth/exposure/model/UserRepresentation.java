package dk.vandborgandersen.auth.exposure.model;

import javax.xml.bind.annotation.XmlRootElement;

import dk.vandborgandersen.auth.model.User;

/**
 * Representation of a User.
 *
 * @author mortena@gmail.com
 */
@XmlRootElement
public class UserRepresentation {

    private String email;

    public UserRepresentation(User user) {
        this.email = user.getEmail();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
