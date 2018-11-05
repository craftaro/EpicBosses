package com.songoda.epicbosses.skills;

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

}
