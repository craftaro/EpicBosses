package net.aminecraftdev.custombosses.utils;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 08-Mar-18
 */
public enum Debug {

    NULL_CHECK("An object was found as null when it should not be null.");

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
