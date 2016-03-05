package dk.vandborgandersen.auth.persistence;

import org.springframework.data.annotation.Id;

/**
 * A transaction log.
 *
 * @author mortena@gmail.com
 */

public class TransactionLog {

    @Id
    private String tId;

    private long timestamp;

    private int level;

    private String message;

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
