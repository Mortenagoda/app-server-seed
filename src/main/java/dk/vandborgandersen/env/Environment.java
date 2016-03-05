package dk.vandborgandersen.env;

/**
 * Environment definition.
 *
 * @author mortena@gmail.com
 */

public class Environment {

    boolean production;
    boolean test;

    public Environment(boolean production) {
        this.production = production;
        this.test = !production;
    }

    public boolean isProduction() {
        return production;
    }

    public boolean isTest() {
        return test;
    }
}
