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
public class GiveTableSubElement {

    @Expose @Getter @Setter private Map<String, Double> items, commands;
    @Expose @Getter @Setter private Integer maxDrops, maxCommands;
    @Expose @Getter @Setter private Boolean randomDrops, randomCommands;
    @Expose @Getter @Setter private Double requiredPercentage;

    public GiveTableSubElement(Map<String, Double> items, Map<String, Double> commands, Integer maxDrops, Integer maxCommands, Boolean randomDrops, Boolean randomCommands, Double requiredPercentage) {
        this.items = items;
        this.commands = commands;
        this.maxDrops = maxDrops;
        this.maxCommands = maxCommands;
        this.randomDrops = randomDrops;
        this.randomCommands = randomCommands;
        this.requiredPercentage = requiredPercentage;
    }

}
