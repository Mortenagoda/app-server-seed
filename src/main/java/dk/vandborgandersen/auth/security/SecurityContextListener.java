package dk.vandborgandersen.auth.security;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

import dk.vandborgandersen.auth.persistence.UserArchivist;

/**
 * Ensures an admin user exists.
 *
 * @author mortena@gmail.com
 */
@Provider
@PreMatching
public class SecurityContextListener implements ServletContextListener {
    private static final Logger LOGGER = Logger.getLogger(SecurityContextListener.class.getName());
    @Inject
    UserArchivist userArchivist;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        // Server stopping
    }
}
