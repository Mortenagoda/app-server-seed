package dk.vandborgandersen.auth.exposure.model;

import java.util.Date;

import dk.vandborgandersen.auth.model.AccessToken;

/**
 * Service exposure for access token.
 *
 * @author mortena@gmail.com
 */

public class AccessTokenRepresentation {
    private String accessToken;
    private long expiryTime;
    private Date issueTimeStamp;

    public AccessTokenRepresentation(String accessToken, long expiryTime, Date issueTimeStamp) {
        this.accessToken = accessToken;
        this.expiryTime = expiryTime;
        this.issueTimeStamp = issueTimeStamp;
    }

    public AccessTokenRepresentation(AccessToken token) {
        this(token.getAccessToken(), token.getExpiryTime(), new Date(token.getIssueTimestamp()));
    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getExpiryTime() {
        return expiryTime;
    }

    public Date getIssueTimeStamp() {
        return issueTimeStamp;
    }
}
