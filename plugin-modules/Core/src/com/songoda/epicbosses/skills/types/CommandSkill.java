package com.songoda.epicbosses.skills.types;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.elements.CommandSkillElement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Nov-18
 */
public class CommandSkill extends Skill {

    @Expose @Getter @Setter private List<CommandSkillElement> commands;

}
