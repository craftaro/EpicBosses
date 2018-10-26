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

    @Expose @Getter @Setter private Map<String, Double> items;
    @Expose @Getter @Setter private Integer maxDrops;
    @Expose @Getter @Setter private Boolean randomDrops;

}
