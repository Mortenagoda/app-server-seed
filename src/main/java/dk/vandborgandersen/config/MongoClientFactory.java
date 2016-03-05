package dk.vandborgandersen.config;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.glassfish.hk2.api.Factory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Factory class for MongoClient. Connects to the correct database.
 *
 * @author mortena@gmail.com
 */

public class MongoClientFactory implements Factory<MongoClient> {
    private static final Logger LOGGER = Logger.getLogger(MongoClientFactory.class.getName());

    private MongoDBStarter mongoDBStarter;

    @Override
    public MongoClient provide() {
        try {
            // todo: remove atwork
            MongoClient mongoClient =
                new MongoClient(new MongoClientURI("mongodb://atwork:password@localhost/appDB?" +
                    "connectTimeoutMS=1000"));
            try {
                MongoOperations mongoOperations = new MongoTemplate(mongoClient, "appDB");
                Set<String> collectionNames = mongoOperations.getCollectionNames();
                for (Iterator<String> iterator = collectionNames.iterator(); iterator.hasNext(); ) {
                    String collection = iterator.next();
                    LOGGER.info("Collection: <" + collection + "> exists.");
                }
            } catch (Exception e) {
                LOGGER.info("Unable to connect to real mongodb, switching to in-memory database.");
                try {
                    mongoDBStarter = new MongoDBStarter();
                    mongoClient = mongoDBStarter.getMongoClient();
                } catch (IOException e1) {
                    throw new RuntimeException("Unable to switch to in memory mongo db, nothing cane be done.", e);
                }
            }

            return mongoClient;
        } catch (UnknownHostException e) {
            throw new RuntimeException("Mongo server incorrect.");
        }
    }

    @Override
    public void dispose(MongoClient mongo) {
        try {
            mongo.close();
            if (mongoDBStarter != null) {
                try {
                    mongoDBStarter.shutdown();
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {

        }
    }
}
