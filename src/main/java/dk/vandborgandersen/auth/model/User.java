package dk.vandborgandersen.auth.model;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;

/**
 * The definition of any user. Email is always the username.
 *
 * @author mortena@gmail.com
 */

public class User {

    @Id
    @NotNull
    private String tId;

    @NotNull
    private String email;

    @NotNull
    private String challengeEncrypted;

    public User() {

    }

    public User(String email, String challenge) {
        this.email = email;
        this.challengeEncrypted = challenge;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChallengeEncrypted() {
        return challengeEncrypted;
    }

    public void setChallengeEncrypted(String challengeEncrypted) {
        this.challengeEncrypted = challengeEncrypted;
    }

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

    @Override
    public String toString() {
        return "User{" +
            "email='" + email + '\'' +
            ", challengeEncrypted='" + challengeEncrypted + '\'' +
            '}';
    }
}
