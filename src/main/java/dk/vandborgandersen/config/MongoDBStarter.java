package dk.vandborgandersen.config;

import java.io.IOException;

import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ExtractedArtifactStoreBuilder;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.RuntimeConfigBuilder;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.extract.ITempNaming;
import de.flapdoodle.embed.process.extract.UserTempNaming;
import de.flapdoodle.embed.process.runtime.Network;

/**
 * In memory MongoDB instance.
 *
 * @author mortena@gmail.com
 */

public class MongoDBStarter {
    private static MongodStarter starter;

    private MongodExecutable executable;
    private MongodProcess process;

    private MongoClient mongoClient;

    public MongoDBStarter() throws IOException {
        setup();
    }

    private void setup() throws IOException {

        ITempNaming executableNaming = new UserTempNaming();

        Command command = Command.MongoD;

        IRuntimeConfig runtimeConfig = new RuntimeConfigBuilder()
            .defaults(command)
            .artifactStore(new ExtractedArtifactStoreBuilder()
                .defaults(command)
                .executableNaming(executableNaming))
            .build();

        IMongodConfig build = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
            .net(new Net(9991, Network.localhostIsIPv6())).build();
        starter = MongodStarter.getInstance(runtimeConfig);
        executable = starter.prepare(build);
        process = executable.start();
        this.mongoClient = new MongoClient("localhost", 9991);
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public void shutdown() {
        this.process.stop();
        this.executable.stop();
    }
}
