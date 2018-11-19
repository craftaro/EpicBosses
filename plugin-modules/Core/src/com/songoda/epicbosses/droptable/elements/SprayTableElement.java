package com.songoda.epicbosses.droptable.elements;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Oct-18
 */
public class SprayTableElement {

    @Expose @Getter @Setter private Map<String, Double> sprayRewards;
    @Expose @Getter @Setter private Boolean randomSprayDrops;
    @Expose @Getter @Setter private Integer sprayMaxDistance, sprayMaxDrops;

    public SprayTableElement(Map<String, Double> sprayRewards, Boolean randomSprayDrops, Integer sprayMaxDistance, Integer sprayMaxDrops) {
        this.sprayRewards = sprayRewards;
        this.randomSprayDrops = randomSprayDrops;
        this.sprayMaxDistance = sprayMaxDistance;
        this.sprayMaxDrops = sprayMaxDrops;
    }

}
