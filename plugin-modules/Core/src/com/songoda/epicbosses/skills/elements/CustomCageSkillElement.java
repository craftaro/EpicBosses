package com.songoda.epicbosses.skills.elements;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.skills.interfaces.IOtherSkillDataElement;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Nov-18
 */
public class CustomCageSkillElement implements IOtherSkillDataElement {

    @Expose @Getter @Setter private String flatType, wallType, insideType;

    public CustomCageSkillElement(String flatType, String wallType, String insideType) {
        this.flatType = flatType;
        this.wallType = wallType;
        this.insideType = insideType;
    }

}
