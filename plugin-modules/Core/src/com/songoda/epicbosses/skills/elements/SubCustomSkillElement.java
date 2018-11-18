package com.songoda.epicbosses.skills.elements;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.utils.BossesGson;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Nov-18
 */
public class SubCustomSkillElement {

    @Expose @Getter @Setter private String type;
    @Expose @Getter @Setter private Double multiplier;
    @Expose @Setter private JsonObject otherSkillData;

    public SubCustomSkillElement(String type, Double multiplier, JsonObject otherSkillData) {
        this.type = type;
        this.multiplier = multiplier;
        this.otherSkillData = otherSkillData;
    }

    public CustomCageSkillElement getCustomCageSkillData() {
        if(getType().equalsIgnoreCase("CAGE")) {
            return BossesGson.get().fromJson(this.otherSkillData, CustomCageSkillElement.class);
        }

        return null;
    }

    public CustomMinionSkillElement getCustomMinionSkillData() {
        if(getType().equalsIgnoreCase("MINION")) {
            return BossesGson.get().fromJson(this.otherSkillData, CustomMinionSkillElement.class);
        }

        return null;
    }

}
