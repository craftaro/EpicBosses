package com.songoda.epicbosses.entity.elements;

import com.google.gson.annotations.Expose;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 21-Oct-18
 */
public class OnDeathMessageElement {

    @Expose
    private String message, positionMessage;
    @Expose
    private Integer radius, onlyShow;

    public OnDeathMessageElement(String message, String positionMessage, Integer radius, Integer onlyShow) {
        this.message = message;
        this.positionMessage = positionMessage;
        this.radius = radius;
        this.onlyShow = onlyShow;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPositionMessage() {
        return this.positionMessage;
    }

    public void setPositionMessage(String positionMessage) {
        this.positionMessage = positionMessage;
    }

    public Integer getRadius() {
        return this.radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public Integer getOnlyShow() {
        return this.onlyShow;
    }

    public void setOnlyShow(Integer onlyShow) {
        this.onlyShow = onlyShow;
    }
}
