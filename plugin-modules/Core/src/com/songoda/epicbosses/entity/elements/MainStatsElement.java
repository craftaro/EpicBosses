package com.songoda.epicbosses.entity.elements;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 14-May-18
 */
public class MainStatsElement {

    @Expose @Getter @Setter private Integer position;
    @Expose @Getter @Setter private String entityType;
    @Expose @Getter @Setter private Double health;
    @Expose @Getter @Setter private String displayName;

    public MainStatsElement(Integer position, String entityType, Double health, String displayName) {
        this.position = position;
        this.entityType = entityType;
        this.health = health;
        this.displayName = displayName;
    }

}
