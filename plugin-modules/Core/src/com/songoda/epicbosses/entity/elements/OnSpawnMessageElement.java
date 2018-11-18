package com.songoda.epicbosses.entity.elements;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 21-Oct-18
 */
public class OnSpawnMessageElement {

    @Expose @Getter @Setter private String message;
    @Expose @Getter @Setter private Integer radius;

    public OnSpawnMessageElement(String message, Integer radius) {
        this.message = message;
        this.radius = radius;
    }

}
