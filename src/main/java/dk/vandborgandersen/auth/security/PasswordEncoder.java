package dk.vandborgandersen.auth.security;

import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * Application helper class for encrypting password.
 *
 * @author mortena@gmail.com
 */

public class PasswordEncoder {
    private StandardPasswordEncoder encoder;

    public PasswordEncoder() {
        String encSecret = System.getProperty("ENC_SECRET");
        if (encSecret == null) {
            encSecret = "vandborgandersen.dk";
        }
        encoder = new StandardPasswordEncoder(encSecret);
    }

    public String encode(String password) {
        return encoder.encode(password);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
