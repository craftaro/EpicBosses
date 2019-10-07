package com.songoda.epicbosses.skills.types;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.managers.BossSkillManager;
import com.songoda.epicbosses.managers.files.SkillsFileManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.interfaces.ISkillHandler;
import com.songoda.epicbosses.utils.Debug;
import org.bukkit.entity.LivingEntity;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Nov-18
 */
public class GroupSkillElement implements ISkillHandler<GroupSkillElement> {

    @Expose
    private List<String> groupedSkills;

    public GroupSkillElement(List<String> groupedSkills) {
        this.groupedSkills = groupedSkills;
    }

    @Override
    public void castSkill(Skill skill, GroupSkillElement groupSkillElement, ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities) {
        List<String> currentGroupedSkills = getGroupedSkills();
        EpicBosses plugin = EpicBosses.getInstance();
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

    public List<String> getGroupedSkills() {
        return this.groupedSkills;
    }

    public void setGroupedSkills(List<String> groupedSkills) {
        this.groupedSkills = groupedSkills;
    }
}
