package com.songoda.epicbosses.droptable.elements;

import com.google.gson.annotations.Expose;

import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Oct-18
 */
public class SprayTableElement {

    @Expose
    private Map<String, Double> sprayRewards;
    @Expose
    private Boolean randomSprayDrops;
    @Expose
    private Integer sprayMaxDistance, sprayMaxDrops;

    public SprayTableElement(Map<String, Double> sprayRewards, Boolean randomSprayDrops, Integer sprayMaxDistance, Integer sprayMaxDrops) {
        this.sprayRewards = sprayRewards;
        this.randomSprayDrops = randomSprayDrops;
        this.sprayMaxDistance = sprayMaxDistance;
        this.sprayMaxDrops = sprayMaxDrops;
    }

    public Map<String, Double> getSprayRewards() {
        return this.sprayRewards;
    }

    public Boolean getRandomSprayDrops() {
        return this.randomSprayDrops;
    }

    public Integer getSprayMaxDistance() {
        return this.sprayMaxDistance;
    }

    public Integer getSprayMaxDrops() {
        return this.sprayMaxDrops;
    }

    public void setSprayRewards(Map<String, Double> sprayRewards) {
        this.sprayRewards = sprayRewards;
    }

    public void setRandomSprayDrops(Boolean randomSprayDrops) {
        this.randomSprayDrops = randomSprayDrops;
    }

    public void setSprayMaxDistance(Integer sprayMaxDistance) {
        this.sprayMaxDistance = sprayMaxDistance;
    }

    public void setSprayMaxDrops(Integer sprayMaxDrops) {
        this.sprayMaxDrops = sprayMaxDrops;
    }
}
