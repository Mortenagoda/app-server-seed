package dk.vandborgandersen.auth.exposure.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Representation for a successfully login.
 *
 * @author mortena@gmail.com
 */
@XmlRootElement
public class LoginSuccessRepresentation {

    private String accessToken;

    private long expiryTime;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(long expiryTime) {
        this.expiryTime = expiryTime;
    }
}
