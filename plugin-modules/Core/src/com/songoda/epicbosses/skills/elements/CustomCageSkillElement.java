package com.songoda.epicbosses.skills.elements;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.skills.interfaces.IOtherSkillDataElement;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Nov-18
 */
public class CustomCageSkillElement implements IOtherSkillDataElement {

    @Expose
    private String flatType, wallType, insideType;
    @Expose
    private int duration;

    public CustomCageSkillElement(String flatType, String wallType, String insideType, int duration) {
        this.flatType = flatType;
        this.wallType = wallType;
        this.insideType = insideType;
        this.duration = duration;
    }

    public String getFlatType() {
        return this.flatType;
    }

    public void setFlatType(String flatType) {
        this.flatType = flatType;
    }

    public String getWallType() {
        return this.wallType;
    }

    public void setWallType(String wallType) {
        this.wallType = wallType;
    }

    public String getInsideType() {
        return this.insideType;
    }

    public void setInsideType(String insideType) {
        this.insideType = insideType;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
