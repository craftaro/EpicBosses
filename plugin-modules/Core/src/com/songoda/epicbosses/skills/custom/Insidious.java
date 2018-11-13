package com.songoda.epicbosses.skills.custom;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.skills.ISkillHandler;
import com.songoda.epicbosses.skills.types.CustomSkill;
import org.bukkit.entity.LivingEntity;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Nov-18
 */
public class Insidious extends CustomSkill implements ISkillHandler {

    public Insidious(String mode, String type, Double radius, String displayName, String customMessage) {
        super(mode, type, radius, displayName, customMessage);
    }

    @Override
    public void castSkill(ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities) {
        Double multiplier = getCustom().getMultiplier();

        if(multiplier == null) multiplier = 2.5;

        double finalMultiplier = multiplier;

        nearbyEntities.forEach(livingEntity -> livingEntity.setFireTicks(((int) finalMultiplier) * 20));
    }
}
