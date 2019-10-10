package com.songoda.epicbosses.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Apr-18
 */
public class NumberUtils {

    private static NumberUtils INSTANCE = new NumberUtils();

    public static NumberUtils get() {
        return INSTANCE;
    }

    public boolean isInt(String string) {
        try {
            Integer.valueOf(string);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public Integer getInteger(String input) {
        if (!isInt(input)) return null;

        return Integer.valueOf(input);
    }

    public int getSquared(int original) {
        if (original == -1) return -1;

        return original * original;
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
        if (!isDouble(input)) return null;

        return Double.valueOf(input);
    }

    public String formatDouble(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###,###.##");

        return decimalFormat.format(d);
    }

    public int getNextAvailablePosition(List<String> keys) {
        if (keys.isEmpty()) return 1;

        List<Integer> currentIds = new ArrayList<>();

        keys.stream().filter(NumberUtils.get()::isInt).forEach(s -> currentIds.add(Integer.valueOf(s)));
        currentIds.sort(Comparator.naturalOrder());

        for (int i = 1; i <= currentIds.size(); i++) {
            if (i < currentIds.get(i - 1)) {
                return i;
            }
        }

        return currentIds.size() + 1;
    }

}
