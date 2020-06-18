package com.songoda.epicbosses.entity.elements;

import com.google.gson.annotations.Expose;

public class HealthBarElement {

    @Expose
    private String text;

    @Expose
    private Integer radius;

    public HealthBarElement(String text, Integer radius) {
        this.text = text;
        this.radius = radius;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }
}