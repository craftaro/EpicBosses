package com.songoda.epicbosses.droptable.elements;

import com.google.gson.annotations.Expose;

import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Oct-18
 */
public class DropTableElement {

    @Expose
    private Map<String, Double> dropRewards;
    @Expose
    private Boolean randomDrops;
    @Expose
    private Integer dropMaxDrops;

    public DropTableElement(Map<String, Double> dropRewards, Boolean randomDrops, Integer dropMaxDrops) {
        this.dropRewards = dropRewards;
        this.randomDrops = randomDrops;
        this.dropMaxDrops = dropMaxDrops;
    }

    public Map<String, Double> getDropRewards() {
        return this.dropRewards;
    }

    public Boolean getRandomDrops() {
        return this.randomDrops;
    }

    public Integer getDropMaxDrops() {
        return this.dropMaxDrops;
    }

    public void setDropRewards(Map<String, Double> dropRewards) {
        this.dropRewards = dropRewards;
    }

    public void setRandomDrops(Boolean randomDrops) {
        this.randomDrops = randomDrops;
    }

    public void setDropMaxDrops(Integer dropMaxDrops) {
        this.dropMaxDrops = dropMaxDrops;
    }
}
