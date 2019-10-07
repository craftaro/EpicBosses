package com.songoda.epicbosses.entity.elements;

import com.google.gson.annotations.Expose;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 14-May-18
 */
public class MainStatsElement {

    @Expose
    private Integer position;
    @Expose
    private String entityType;
    @Expose
    private Double health;
    @Expose
    private String displayName;

    public MainStatsElement(Integer position, String entityType, Double health, String displayName) {
        this.position = position;
        this.entityType = entityType;
        this.health = health;
        this.displayName = displayName;
    }

    public Integer getPosition() {
        return this.position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getEntityType() {
        return this.entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Double getHealth() {
        return this.health;
    }

    public void setHealth(Double health) {
        this.health = health;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
