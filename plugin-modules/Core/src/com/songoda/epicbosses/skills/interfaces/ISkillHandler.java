package com.songoda.epicbosses.skills.interfaces;

import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.skills.Skill;
import org.bukkit.entity.LivingEntity;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Nov-18
 */
public interface ISkillHandler<T> {

    void castSkill(Skill skill, T t, ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities);

}
