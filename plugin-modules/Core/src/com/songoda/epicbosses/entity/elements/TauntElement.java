package com.songoda.epicbosses.entity.elements;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 14-May-18
 */
public class TauntElement {

    @Expose
    private Integer delay, radius;
    @Expose
    private List<String> taunts;

    public TauntElement(Integer delay, Integer radius, List<String> taunts) {
        this.delay = delay;
        this.radius = radius;
        this.taunts = taunts;
    }

    public Integer getDelay() {
        return this.delay;
    }

    public Integer getRadius() {
        return this.radius;
    }

    public List<String> getTaunts() {
        return this.taunts;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public void setTaunts(List<String> taunts) {
        this.taunts = taunts;
    }
}
