package com.songoda.epicbosses.utils;

import java.util.Random;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Oct-18
 */
public class RandomUtils {

    private static RandomUtils INSTANCE = new RandomUtils();

    private Random random = new Random();

    public int getRandomNumber() {
        return this.random.nextInt(100);
    }

    public double getRandomDecimalNumber() {
        double amount = getRandomNumber();

        amount += this.random.nextDouble();

        return amount;
    }

    public static RandomUtils get() {
        return INSTANCE;
    }

}
