package net.aminecraftdev.custombosses.droptable.elements;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Oct-18
 */
public class SprayTableElement extends RewardsTableElement {

    @Expose @Getter @Setter private Map<String, Double> sprayRewards;
    @Expose @Getter @Setter private Integer sprayMaxDistance, sprayMaxDrops;

}
