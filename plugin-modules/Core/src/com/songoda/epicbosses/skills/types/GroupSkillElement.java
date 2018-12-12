package com.songoda.epicbosses.skills.types;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.managers.BossSkillManager;
import com.songoda.epicbosses.managers.files.SkillsFileManager;
import com.songoda.epicbosses.skills.ISkillHandler;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.utils.Debug;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;

import java.util.List;
import java.util.Map;

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
    public boolean doesUseMultiplier() {
        return false;
    }

    @Override
    public Map<String, Class<?>> getOtherSkillData() {
        return null;
    }

    @Override
    public void castSkill(Skill skill, GroupSkillElement groupSkillElement, ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities) {
        List<String> currentGroupedSkills = getGroupedSkills();
        CustomBosses plugin = CustomBosses.get();
        SkillsFileManager skillsFileManager = plugin.getSkillsFileManager();
        BossSkillManager bossSkillManager = plugin.getBossSkillManager();

        currentGroupedSkills.forEach(string -> {
            Skill innerSkill = skillsFileManager.getSkill(string);

            if(innerSkill == null) {
                Debug.SKILL_NOT_FOUND.debug();
                return;
            }

            bossSkillManager.handleSkill(null, skill, nearbyEntities, activeBossHolder, false, true);
        });
    }
}
