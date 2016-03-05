package dk.vandborgandersen.auth.exposure.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Representation used for adding a new user.
 *
 * @author mortena@gmail.com
 */
@XmlRootElement
public class AddUserRepresentation {

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AddUserRepresentation{" +
            "email='" + email + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
