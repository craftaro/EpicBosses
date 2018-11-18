package com.songoda.epicbosses.skills.custom;

import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.skills.CustomSkillHandler;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.types.CustomSkillElement;
import org.bukkit.entity.LivingEntity;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Nov-18
 */
public class Warp extends CustomSkillHandler {

    @Override
    public void castSkill(Skill skill, CustomSkillElement customSkillElement, ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities) {
        LivingEntity target = nearbyEntities.get(0);

        activeBossHolder.getLivingEntity().teleport(target);
    }
}
