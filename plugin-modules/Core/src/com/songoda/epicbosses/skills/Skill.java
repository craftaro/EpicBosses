package com.songoda.epicbosses.skills;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Nov-18
 */
public class Skill {

    @Expose
    private String mode, type, displayName, customMessage;
    @Expose
    private Double radius;
    @Expose
    private JsonObject customData;

    public Skill(String mode, String type, Double radius, String displayName, String customMessage) {
        this.mode = mode;
        this.type = type;
        this.radius = radius;
        this.displayName = displayName;
        this.customMessage = customMessage;
    }

    public String getMode() {
        return this.mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCustomMessage() {
        return this.customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }

    public Double getRadius() {
        return this.radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public JsonObject getCustomData() {
        return this.customData;
    }

    public void setCustomData(JsonObject customData) {
        this.customData = customData;
    }
}
