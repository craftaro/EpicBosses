package com.songoda.epicbosses.utils;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 31-Dec-18
 */
public class ObjectUtils {

    public static <T> T getValue(T input, T defaultValue) {
        if (input == null) return defaultValue;

        return input;
    }

}
