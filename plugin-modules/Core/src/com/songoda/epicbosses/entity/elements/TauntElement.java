package com.songoda.epicbosses.entity.elements;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 14-May-18
 */
public class TauntElement {

    @Expose @Getter @Setter private Integer delay, radius;
    @Expose @Getter @Setter private List<String> taunts;

    public TauntElement(Integer delay, Integer radius, List<String> taunts) {
        this.delay = delay;
        this.radius = radius;
        this.taunts = taunts;
    }

}
