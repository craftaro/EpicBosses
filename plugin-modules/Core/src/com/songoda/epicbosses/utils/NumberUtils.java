package com.songoda.epicbosses.utils;

import java.text.DecimalFormat;

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

    public Integer getInteger(String input) {
        if(!isInt(input)) return null;

        return Integer.valueOf(input);
    }

    public boolean isDouble(String string) {
        try {
            Double.valueOf(string);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public Double getDouble(String input) {
        if(!isDouble(input)) return null;

        return Double.valueOf(input);
    }

    public String formatDouble(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###,###.##");

        return decimalFormat.format(d);
    }

    public static NumberUtils get() {
        return INSTANCE;
    }

}
