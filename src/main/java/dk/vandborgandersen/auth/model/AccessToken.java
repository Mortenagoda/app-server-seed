package dk.vandborgandersen.auth.model;

import org.springframework.data.annotation.Id;

/**
 * Access token registration.
 *
 * @author mortena@gmail.com
 */

public class AccessToken {

    @Id
    private String tId;

    private String userTId;
    private String accessToken;
    private long expiryTime;
    private long issueTimestamp;

    public AccessToken() {
    }

    public AccessToken(String userTId, String accessToken, long expiryTime, long issueTimestamp) {
        this.userTId = userTId;
        this.accessToken = accessToken;
        this.expiryTime = expiryTime;
        this.issueTimestamp = issueTimestamp;
    }

    public final String gettId() {
        return tId;
    }

    public final void settId(String tId) {
        this.tId = tId;
    }

    public String getUserTId() {
        return userTId;
    }

    public void setUserTId(String userTId) {
        this.userTId = userTId;
    }

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

    public long getIssueTimestamp() {
        return issueTimestamp;
    }

    public void setIssueTimestamp(long issueTimestamp) {
        this.issueTimestamp = issueTimestamp;
    }
}
