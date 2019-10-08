package com.songoda.epicbosses.entity.elements;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 14-May-18
 */
public class SkillsElement {

    @Expose
    private Double overallChance;
    @Expose
    private String masterMessage;
    @Expose
    private List<String> skills;

    public SkillsElement(Double overallChance, String masterMessage, List<String> skills) {
        this.overallChance = overallChance;
        this.masterMessage = masterMessage;
        this.skills = skills;
    }

    public Double getOverallChance() {
        return this.overallChance;
    }

    public void setOverallChance(Double overallChance) {
        this.overallChance = overallChance;
    }

    public String getMasterMessage() {
        return this.masterMessage;
    }

    public void setMasterMessage(String masterMessage) {
        this.masterMessage = masterMessage;
    }

    public List<String> getSkills() {
        return this.skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
}
