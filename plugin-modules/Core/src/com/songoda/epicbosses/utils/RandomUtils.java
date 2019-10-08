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

    public static RandomUtils get() {
        return INSTANCE;
    }

    public boolean preformRandomAction() {
        int rand = getRandomNumber(2);

        return rand <= 0;
    }

    public int getRandomNumber() {
        return this.random.nextInt(100);
    }

    public int getRandomNumber(int maximum) {
        return this.random.nextInt(maximum);
    }

    public double getRandomDecimalNumber() {
        double amount = getRandomNumber();

        amount += this.random.nextDouble();

        return amount;
    }

    public boolean canPreformAction(double chanceOfSuccess) {
        double randomChance = getRandomDecimalNumber();

        return (randomChance <= chanceOfSuccess);
    }

}
