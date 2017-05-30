package net.aminecraftdev.custombosses.innerapi;

import java.text.DecimalFormat;

/**
 * Created by charl on 28-Apr-17.
 */
public class NumberUtils {

    public static final String formatDouble(double d) {
        DecimalFormat format = new DecimalFormat("###,###,###,###,###.##");

        return format.format(d);
    }

    public static final String formatTime(int time) {
        int hours = time / 3600;
        int remainder = time % 3600;
        int minutes = remainder / 60;
        int seconds = remainder % 60;
        String disHour = (hours < 10 ? "0" : "") + hours;
        String disMinu = (minutes < 10 ? "0" : "") + minutes;
        String disSeco = (seconds < 10 ? "0" : "") + seconds;
        String formatted = "";

        if(hours != 0) formatted += disHour + " hours ";
        if(minutes != 0) formatted += disMinu + " minutes ";
        if(seconds != 0) formatted += disSeco + " seconds.";

        return formatted;
    }

    public static final boolean isStringInteger(String s) {
        try {
            Integer.valueOf(s);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public static final boolean isStringDouble(String s) {
        try {
            Double.valueOf(s);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

}