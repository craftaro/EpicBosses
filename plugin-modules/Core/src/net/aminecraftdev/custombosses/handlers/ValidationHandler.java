package net.aminecraftdev.custombosses.handlers;

import net.aminecraftdev.custombosses.utils.Debug;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 08-Mar-18
 */
public class ValidationHandler {

    public static boolean isNull(Object object) {
        if(object == null) {
            Debug.NULL_CHECK.debug();
            return true;
        }

        return false;
    }


}
