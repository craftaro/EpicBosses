package com.songoda.epicbosses.utils.time;

public class TimeUtil {

    /**
     * Returns the amount of time in the
     * requested TimeUnit from a formatted string
     *
     * @param string String
     * @param timeUnit TimeUnit
     * @return double
     * @throws IllegalArgumentException when the string is formatted wrongly
     */
    public static double getTime(String string, TimeUnit timeUnit) throws IllegalArgumentException {
        double seconds = 0;

        String timeString = string.trim().toLowerCase();

        if(timeString.contains(" ")) {
            String[] split = timeString.split(" ");

            for(String line : split) {
                int index = 0;
                while(isInteger(String.valueOf(line.charAt(index)))) {
                    index++;
                }

                int time = Integer.parseInt(line.substring(0, index));

                String unit = line.substring(index+1, line.length());
                TimeUnit tempUnit = TimeUnit.fromString(unit);
                if(tempUnit == null) throw new IllegalArgumentException("Invalid time format: " + unit);

                seconds += tempUnit.to(TimeUnit.SECONDS, time);
            }
        } else {
            int index = 0;
            int prevIndex = 0;
            while(index < timeString.length()-1) {
                double time;
                while(isInteger(String.valueOf(timeString.charAt(index)))) {
                    index++;
                }
                time = Integer.parseInt(timeString.substring(prevIndex, index));
                prevIndex = index;

                while(!isInteger(String.valueOf(timeString.charAt(index)))) {
                    index++;
                    if(index >= timeString.length()) break;
                }
                String unit = timeString.substring(prevIndex, index);
                TimeUnit tempUnit = TimeUnit.fromString(unit);
                if(tempUnit == null) throw new IllegalArgumentException("Invalid time format: " + unit);

                seconds += tempUnit.to(TimeUnit.SECONDS, time);

                prevIndex = index;
            }
        }

        return TimeUnit.SECONDS.to(timeUnit, seconds);
    }

    /**
     * Returns the formatted time string
     * belonging to this amount of time
     *
     * @param unit TimeUnit
     * @param a int amount of time
     * @return String
     */
    public static String getFormattedTime(TimeUnit unit, int a) {
        return getFormattedTime(unit, a, null);
    }

    /**
     * Returns the formatted time string
     * belonging to this amount of time
     *
     * @param unit TimeUnit
     * @param a int amount of time
     * @param splitter String
     * @return String
     */
    public static String getFormattedTime(TimeUnit unit, int a, String splitter) {
        int amount = a;
        int years = (int) unit.to(TimeUnit.YEARS, amount);
        amount -= TimeUnit.YEARS.to(unit, years);
        int months = (int) unit.to(TimeUnit.MONTHS, amount);
        amount -= TimeUnit.MONTHS.to(unit, months);
        int weeks = (int) unit.to(TimeUnit.WEEKS, amount);
        amount -= TimeUnit.WEEKS.to(unit, weeks);
        int days = (int) unit.to(TimeUnit.DAYS, amount);
        amount -= TimeUnit.DAYS.to(unit, days);
        int hours = (int) unit.to(TimeUnit.HOURS, amount);
        amount -= TimeUnit.HOURS.to(unit, hours);
        int minutes = (int) unit.to(TimeUnit.MINUTES, amount);
        amount -= TimeUnit.MINUTES.to(unit, minutes);
        int seconds = (int) unit.to(TimeUnit.SECONDS, amount);

        StringBuilder sb = new StringBuilder();

        if(splitter == null) {
            if (years > 0) {
                sb.append(years).append(" year").append(years != 1 ? "s" : "").append(", ");
            }
            if (months > 0 || sb.length() > 0) {
                sb.append(months).append(" month").append(months != 1 ? "s" : "").append(", ");
            }
            if (weeks > 0 || sb.length() > 0) {
                sb.append(weeks).append(" week").append(weeks != 1 ? "s" : "").append(", ");
            }
            if (days > 0 || sb.length() > 0) {
                sb.append(days).append(" day").append(days != 1 ? "s" : "").append(", ");
            }
            if (hours > 0 || sb.length() > 0) {
                sb.append(hours).append(" hour").append(hours != 1 ? "s" : "").append(", ");
            }
            if (minutes > 0 || sb.length() > 0) {
                sb.append(minutes).append(" minute").append(minutes != 1 ? "s" : "").append(" ");
            }
            if (seconds > 0 || sb.length() > 0) {
                sb.append(minutes > 0 ? "and " : "").append(seconds).append(" second").append(seconds != 1 ? "s" : "");
            }
        } else {
            if(years > 0) {
                sb.append(years).append(splitter);
            }
            if(months > 0 || sb.length() > 0) {
                sb.append(months).append(splitter);
            }
            if(weeks > 0 || sb.length() > 0) {
                sb.append(weeks).append(splitter);
            }
            if(days > 0 || sb.length() > 0) {
                sb.append(days).append(splitter);
            }
            if(hours > 0 || sb.length() > 0) {
                sb.append(hours).append(splitter);
            }
            if(minutes > 0 || sb.length() > 0) {
                sb.append(minutes).append(splitter);
            }
            if(seconds > 0 || sb.length() > 0) {
                sb.append(seconds);
            }
        }

        return sb.toString();
    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

}