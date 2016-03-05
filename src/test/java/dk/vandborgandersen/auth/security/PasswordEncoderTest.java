package dk.vandborgandersen.auth.security;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests the PasswordEncoder. Encode and matches methods should be able to validate password.
 *
 * @author mortena@gmail.com
 */

public class PasswordEncoderTest {

    @Test
    public void testPassword() throws InterruptedException {
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        String encodedPassword = passwordEncoder.encode("password");
        Thread.sleep(9800);
        boolean matched = passwordEncoder.matches("password", encodedPassword);
        assertTrue(matched);
    }
}
