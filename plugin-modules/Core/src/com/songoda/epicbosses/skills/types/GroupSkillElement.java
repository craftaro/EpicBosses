package com.songoda.epicbosses.skills.types;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.skills.ISkillHandler;
import com.songoda.epicbosses.skills.Skill;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Nov-18
 */
public class GroupSkillElement implements ISkillHandler<GroupSkillElement> {

    @Expose @Getter @Setter private List<String> groupedSkills;

    public GroupSkillElement(List<String> groupedSkills) {
        this.groupedSkills = groupedSkills;
    }

    @Override
    public void castSkill(Skill skill, GroupSkillElement groupSkillElement, ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities) {
        //TODO
    }
}
