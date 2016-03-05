package dk.vandborgandersen.exposure.model;

/**
 * Standard exposure for errors.
 *
 * @author mortena@gmail.com
 */

public class ErrorExposure {
    private int errorCode;
    private String message;

    public ErrorExposure(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
