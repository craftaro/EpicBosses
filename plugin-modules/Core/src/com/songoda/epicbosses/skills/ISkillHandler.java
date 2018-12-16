package com.songoda.epicbosses.skills;

import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.skills.types.CustomSkillElement;
import org.bukkit.entity.LivingEntity;

import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Nov-18
 */
public interface ISkillHandler<T> {

    String getSkillName();

    boolean doesUseMultiplier();

    Map<String, Class<?>> getOtherSkillData();

    void castSkill(Skill skill, T t, ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities);

}
