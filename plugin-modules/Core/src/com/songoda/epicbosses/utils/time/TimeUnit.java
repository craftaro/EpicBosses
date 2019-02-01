package com.songoda.epicbosses.utils.time;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 23-May-17
 *
 * Makes it easy to convert
 * time
 */
public enum TimeUnit {

    YEARS(31556926),
    MONTHS(2629744),
    WEEKS(604800),
    DAYS(86400),
    HOURS(3600),
    MINUTES(60),
    SECONDS(1),
    TICK(0.05),
    MILLISECONDS(0.001);

    private double seconds;

    TimeUnit(double seconds) {
        this.seconds = seconds;
    }

    /**
     * Returns a double, representing
     * the amount of time in the requested
     * TimeUnit
     *
     * @param timeUnit TimeUnit
     * @param input double
     * @return double
     */
    public double to(TimeUnit timeUnit, double input) {
        return (input*seconds) / timeUnit.getSeconds();
    }

    /**
     * Returns the amound of seconds
     * that fit into this TimeUnit
     *
     * @return double
     */
    public double getSeconds() {
        return seconds;
    }

    /**
     * Returns the TimeUnit that belongs
     * to the specified String
     *
     * @param string TimeUnit
     * @return TimeUnit
     */
    public static TimeUnit fromString(String string) {
        switch (string.toLowerCase()) {
            case "y":
            case "year":
            case "years":
                return TimeUnit.YEARS;
            case "m":
            case "month":
            case "months":
                return TimeUnit.MONTHS;
            case "w":
            case "week":
            case "weeks":
                return TimeUnit.WEEKS;
            case "d":
            case "day":
            case "days":
                return TimeUnit.DAYS;
            case "h":
            case "hour":
            case "hours":
                return TimeUnit.HOURS;
            case "mins":
            case "min":
            case "minute":
            case "minutes":
                return TimeUnit.MINUTES;
            case "s":
            case "sec":
            case "secs":
            case "second":
            case "seconds":
                return TimeUnit.SECONDS;
            case "ms":
            case "millis":
            case "millisecs":
            case "millisecond":
            case "milliseconds":
                return TimeUnit.MILLISECONDS;
            default:
                return null;
        }
    }

}