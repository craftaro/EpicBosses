package net.aminecraftdev.custombosses.innerapi;

import java.util.Random;

/**
 * Created by charl on 28-Apr-17.
 */
public class RandomUtils {

    private static final Random RANDOM;

    static {
        RANDOM = new Random();
    }

    public static Random getRandom() {
        return RANDOM;
    }

    public static int getRandomInt(int input) {
        return RANDOM.nextInt(input);
    }

}
