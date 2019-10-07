package com.songoda.epicbosses.entity.elements;

import com.google.gson.annotations.Expose;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 21-Oct-18
 */
public class OnSpawnMessageElement {

    @Expose
    private String message;
    @Expose
    private Integer radius;

    public OnSpawnMessageElement(String message, Integer radius) {
        this.message = message;
        this.radius = radius;
    }

    public String getMessage() {
        return this.message;
    }

    public Integer getRadius() {
        return this.radius;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }
}
