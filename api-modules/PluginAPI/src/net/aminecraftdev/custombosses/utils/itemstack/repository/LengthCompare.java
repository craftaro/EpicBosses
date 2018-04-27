package net.aminecraftdev.custombosses.utils.itemstack.repository;

import java.util.Comparator;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Apr-18
 */
public class LengthCompare implements Comparator<String> {

    protected static final LengthCompare INSTANCE = new LengthCompare();

    private LengthCompare() {
        super();
    }

    @Override
    public int compare(String o1, String o2) {
        return o1.length() - o2.length();
    }
}
