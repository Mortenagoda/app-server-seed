package dk.vandborgandersen.env;

/**
 * Provider class for deciding environment.
 *
 * @author mortena@gmail.com
 */

public class EnvProvider {

    private EnvProvider() {
        // No instances please
    }

    public static Environment createEnvironment() {
        boolean isProduction = false;
        String env = System.getProperty("ENV");
        if (env != null && env.equalsIgnoreCase("PROD")) {
            isProduction = true;
        } else {
            isProduction = false;
        }

        return new Environment(isProduction);
    }
}
