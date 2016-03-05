package dk.vandborgandersen.config;

import javax.inject.Inject;

import com.mongodb.MongoClient;
import org.glassfish.hk2.api.Factory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Factory for MongoOperations.
 *
 * @author mortena@gmail.com
 */

public class MongoOperationsFactory implements Factory<MongoOperations> {

    @Inject
    MongoClient mongoClient;

    @Override
    public MongoOperations provide() {
        return new MongoTemplate(mongoClient, "appDB");
    }

    @Override
    public void dispose(MongoOperations mongoOperations) {
        // Nothing to do - probably
    }
}
