package net.aminecraftdev.custombosses.utils;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 08-Mar-18
 */
public enum Debug {

    NULL_CHECK("An object was found as null when it should not be null."),
    NULL_ENTITY_TYPE("The {0} boss or minion has got an invalid entity type."),

    MAX_HEALTH("You cannot set the max health higher than {0}. You can adjust your max health in the spigot.yml file and restart your server to increase this."),
    MECHANIC_APPLICATION_FAILED("Some mechanics have failed to be applied. It got stuck at {0} mechanic."),

    ATTEMPTED_TO_SPAWN_WHILE_DISABLED("The {0} boss/minion attempted to spawn while editing is enabled."),
    FAILED_ATTEMPT_TO_SPAWN_BOSS("A boss has attempted to spawn but cannot spawn for the following reason: \n{0}");

    private String message;

    Debug(String message) {
        this.message = message;
    }

    public void debug(Object... objects) {
        int current = 0;
        String message = this.message;

        for(Object object : objects) {
            String placeholder = "{" + current + "}";

            message = message.replace(placeholder, object.toString());
            current += 1;
        }

        // TODO: Check config if debug is enabled and print message

        throw new IllegalArgumentException(message);
    }

}
