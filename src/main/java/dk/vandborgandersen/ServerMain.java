package dk.vandborgandersen;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

import javax.ws.rs.core.UriBuilder;

import dk.vandborgandersen.config.ApplicationConfig;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

/**
 * Server main method. Starts a embedded servlet container.
 *
 * @author mortena@gmail.com
 */

public class ServerMain {
    public static final URI BASE_URI = getBaseURI();
    private static final Logger LOGGER = Logger.getLogger(ServerMain.class.getName());

    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost").port(9990).path("/").build();
    }

    public static void main(String[] args) throws IOException {
        startServer();
    }

    public static HttpServer startServer() throws IOException {
        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, new ApplicationConfig());
        httpServer.start();
        return httpServer;
    }


}
