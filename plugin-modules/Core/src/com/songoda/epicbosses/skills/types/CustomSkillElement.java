package com.songoda.epicbosses.skills.types;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.skills.elements.SubCustomSkillElement;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Nov-18
 */
public class CustomSkillElement {

    @Expose @Getter @Setter private SubCustomSkillElement custom;

    public CustomSkillElement(SubCustomSkillElement subCustomSkillElement) {
        this.custom = subCustomSkillElement;
    }
}
