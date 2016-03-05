package dk.vandborgandersen.config;

import dk.vandborgandersen.auth.security.AuthFilter;
import dk.vandborgandersen.exposure.ExceptionMapper;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

/**
 * Application configuration.
 */
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        property("contextConfigLocation", "classpath:META-INF/applicationContext.xml");
        register(RequestContextFilter.class);
        register(ExceptionMapper.class);
        register(new ApplicationBinder());
        register(RolesAllowedDynamicFeature.class);
        register(AuthFilter.class);
        packages(true, "dk.vandborgandersen");
        packages(true, "com.theoryinpractise.halbuilder.jaxrs");
        packages(true, "com.theoryinpractise.halbuilder.jersey");
    }
}
