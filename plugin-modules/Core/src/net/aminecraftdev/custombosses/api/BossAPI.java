package net.aminecraftdev.custombosses.api;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Mar-18
 *
 */
public class BossAPI {

    private static BossAPI instance;

    public BossAPI() {
        instance = this;
    }



    public static BossAPI get() {
        return instance;
    }

}
