package dk.vandborgandersen.env;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import dk.vandborgandersen.auth.persistence.TransactionLogArchivist;

/**
 * Transaction logger.
 *
 * @author mortena@gmail.com
 */
@Singleton
public class TransactionLogger {
    private static final Logger LOGGER = Logger.getLogger(TransactionLogger.class.getName());

    @Inject
    TransactionLogArchivist archivist;

    public TransactionLogger() {

    }

    public void info(String msg) {
        log(msg, Level.INFO);
    }

    public void severe(String msg) {
        log(msg, Level.SEVERE);
    }

    public void fine(String msg) {
        log(msg, Level.FINE);
    }

    private void log(String msg, Level level) {
        try {
            archivist.create(msg, level);
        } catch (Throwable e) {
            LOGGER.log(Level.SEVERE, "Problem creating translog.", e);
        }
    }
}
