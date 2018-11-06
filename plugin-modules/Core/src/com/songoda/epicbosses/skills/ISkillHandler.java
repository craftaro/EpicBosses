package com.songoda.epicbosses.skills;

import com.songoda.epicbosses.holder.ActiveBossHolder;
import org.bukkit.entity.LivingEntity;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Nov-18
 */
public interface ISkillHandler {

    void castSkill(ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities);

}
