package com.songoda.epicbosses.skills.elements;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.utils.BossesGson;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Nov-18
 */
public class SubCustomSkillElement {

    @Expose
    private String type;
    @Expose
    private Double multiplier;
    @Expose
    private JsonObject otherSkillData;

    public SubCustomSkillElement(String type, Double multiplier, JsonObject otherSkillData) {
        this.type = type;
        this.multiplier = multiplier;
        this.otherSkillData = otherSkillData;
    }

    public CustomCageSkillElement getCustomCageSkillData() {
        if (getType().equalsIgnoreCase("CAGE")) {
            return BossesGson.get().fromJson(this.otherSkillData, CustomCageSkillElement.class);
        }

        return null;
    }

    public CustomMinionSkillElement getCustomMinionSkillData() {
        if (getType().equalsIgnoreCase("MINIONS")) {
            return BossesGson.get().fromJson(this.otherSkillData, CustomMinionSkillElement.class);
        }

        return null;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getMultiplier() {
        return this.multiplier;
    }

    public void setMultiplier(Double multiplier) {
        this.multiplier = multiplier;
    }

    public void setOtherSkillData(JsonObject otherSkillData) {
        this.otherSkillData = otherSkillData;
    }
}
