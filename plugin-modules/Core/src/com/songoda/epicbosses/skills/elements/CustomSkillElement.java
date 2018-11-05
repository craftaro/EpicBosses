package com.songoda.epicbosses.skills.elements;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Nov-18
 */
public class CustomSkillElement {

    @Expose @Getter @Setter private String type;
    @Expose @Getter @Setter private Double multiplier;

}
