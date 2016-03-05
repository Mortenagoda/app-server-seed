package dk.vandborgandersen.config;

import javax.inject.Singleton;

import com.mongodb.MongoClient;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import com.theoryinpractise.halbuilder.standard.StandardRepresentationFactory;
import dk.vandborgandersen.auth.persistence.AccessTokenArchivist;
import dk.vandborgandersen.auth.persistence.TransactionLogArchivist;
import dk.vandborgandersen.auth.persistence.UserArchivist;
import dk.vandborgandersen.auth.security.PasswordEncoder;
import dk.vandborgandersen.env.TransactionLogger;
import dk.vandborgandersen.exposure.RepresentationHelper;
import dk.vandborgandersen.life.persistence.ShoppingListArchivist;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.springframework.data.mongodb.core.MongoOperations;

public class ApplicationBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(StandardRepresentationFactory.class).to(RepresentationFactory.class).in(Singleton.class);
        bind(UserArchivist.class).to(UserArchivist.class).in(Singleton.class);
        bind(AccessTokenArchivist.class).to(AccessTokenArchivist.class).in(Singleton.class);
        bind(PasswordEncoder.class).to(PasswordEncoder.class).in(Singleton.class);
        bind(TransactionLogArchivist.class).to(TransactionLogArchivist.class).in(Singleton.class);
        bind(TransactionLogger.class).to(TransactionLogger.class).in(Singleton.class);
        bind(RepresentationHelper.class).to(RepresentationHelper.class).in(Singleton.class);
        bind(ShoppingListArchivist.class).to(ShoppingListArchivist.class).in(Singleton.class);
        bindFactory(MongoClientFactory.class).to(MongoClient.class);
        bindFactory(MongoOperationsFactory.class).to(MongoOperations.class).in(Singleton.class);
    }
}
