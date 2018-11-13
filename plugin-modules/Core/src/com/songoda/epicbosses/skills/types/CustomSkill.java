package com.songoda.epicbosses.skills.types;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.skills.ISkillHandler;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.elements.CustomSkillElement;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Nov-18
 */
public class CustomSkill extends Skill {

    protected static final CustomBosses PLUGIN = CustomBosses.get();

    @Expose @Getter @Setter private CustomSkillElement custom;

    public CustomSkill(String mode, String type, Double radius, String displayName, String customMessage) {
        super(mode, type, radius, displayName, customMessage);
    }
}
