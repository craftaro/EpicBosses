package net.aminecraftdev.custombosses.utils;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Apr-18
 */
public class NumberUtils {

    private static NumberUtils INSTANCE = new NumberUtils();

    public boolean isInt(String string) {
        try {
            Integer.valueOf(string);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public boolean isDouble(String string) {
        try {
            Double.valueOf(string);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public static NumberUtils get() {
        return INSTANCE;
    }

}
