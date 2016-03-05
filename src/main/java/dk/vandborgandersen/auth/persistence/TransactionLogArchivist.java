package dk.vandborgandersen.auth.persistence;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.springframework.data.mongodb.core.MongoOperations;

/**
 * Archivist for transaction logs.
 *
 * @author mortena@gmail.com
 */

public class TransactionLogArchivist {
    private static final Logger LOGGER = Logger.getLogger(TransactionLogArchivist.class.getName());
    private static final String TRANSACTION_LOG = "TransactionLog";

    @Inject
    private MongoOperations mongoOperations;

    public void create(String msg, Level level) {
        try {
            TransactionLog transactionLog = new TransactionLog();
            transactionLog.setLevel(level.intValue());
            transactionLog.setMessage(msg);
            transactionLog.setTimestamp(System.currentTimeMillis());
            mongoOperations.insert(transactionLog, TRANSACTION_LOG);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Transaction log could not be persisted.", e);
        }
    }


}
