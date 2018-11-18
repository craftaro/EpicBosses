package com.songoda.epicbosses.skills;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Nov-18
 */
public class Skill {

    @Expose @Getter @Setter private String mode, type, displayName, customMessage;
    @Expose @Getter @Setter private Double radius;
    @Expose @Getter @Setter private JsonObject customData;

    public Skill(String mode, String type, Double radius, String displayName, String customMessage) {
        this.mode = mode;
        this.type = type;
        this.radius = radius;
        this.displayName = displayName;
        this.customMessage = customMessage;
    }

}
