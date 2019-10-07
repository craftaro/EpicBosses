package com.songoda.epicbosses.droptable.elements;

import com.google.gson.annotations.Expose;

import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Oct-18
 */
public class GiveTableSubElement {

    @Expose
    private Map<String, Double> items, commands;
    @Expose
    private Integer maxDrops, maxCommands;
    @Expose
    private Boolean randomDrops, randomCommands;
    @Expose
    private Double requiredPercentage;

    public GiveTableSubElement(Map<String, Double> items, Map<String, Double> commands, Integer maxDrops, Integer maxCommands, Boolean randomDrops, Boolean randomCommands, Double requiredPercentage) {
        this.items = items;
        this.commands = commands;
        this.maxDrops = maxDrops;
        this.maxCommands = maxCommands;
        this.randomDrops = randomDrops;
        this.randomCommands = randomCommands;
        this.requiredPercentage = requiredPercentage;
    }

    public Map<String, Double> getItems() {
        return this.items;
    }

    public void setItems(Map<String, Double> items) {
        this.items = items;
    }

    public Map<String, Double> getCommands() {
        return this.commands;
    }

    public void setCommands(Map<String, Double> commands) {
        this.commands = commands;
    }

    public Integer getMaxDrops() {
        return this.maxDrops;
    }

    public void setMaxDrops(Integer maxDrops) {
        this.maxDrops = maxDrops;
    }

    public Integer getMaxCommands() {
        return this.maxCommands;
    }

    public void setMaxCommands(Integer maxCommands) {
        this.maxCommands = maxCommands;
    }

    public Boolean getRandomDrops() {
        return this.randomDrops;
    }

    public void setRandomDrops(Boolean randomDrops) {
        this.randomDrops = randomDrops;
    }

    public Boolean getRandomCommands() {
        return this.randomCommands;
    }

    public void setRandomCommands(Boolean randomCommands) {
        this.randomCommands = randomCommands;
    }

    public Double getRequiredPercentage() {
        return this.requiredPercentage;
    }

    public void setRequiredPercentage(Double requiredPercentage) {
        this.requiredPercentage = requiredPercentage;
    }
}
