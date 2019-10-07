package com.songoda.epicbosses.skills.types;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.skills.elements.SubCustomSkillElement;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Nov-18
 */
public class CustomSkillElement {

    @Expose
    private SubCustomSkillElement custom;

    public CustomSkillElement(SubCustomSkillElement subCustomSkillElement) {
        this.custom = subCustomSkillElement;
    }

    public SubCustomSkillElement getCustom() {
        return this.custom;
    }

    public void setCustom(SubCustomSkillElement custom) {
        this.custom = custom;
    }
}
