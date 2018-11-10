package com.songoda.epicbosses.skills.elements;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Nov-18
 */
public class CustomCageSkillElement {

    @Expose @Getter @Setter private String flatType, wallType, insideType;

}
