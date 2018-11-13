package com.songoda.epicbosses.skills.custom;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.skills.ISkillHandler;
import com.songoda.epicbosses.skills.types.CustomSkill;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Nov-18
 */
public class Knockback extends CustomSkill implements ISkillHandler {

    public Knockback(String mode, String type, Double radius, String displayName, String customMessage) {
        super(mode, type, radius, displayName, customMessage);
    }

    @Override
    public void castSkill(ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities) {
        Double multiplier = getCustom().getMultiplier();

        if(multiplier == null) multiplier = 2.5;

        double finalMultiplier = multiplier;
        Location bossLocation = activeBossHolder.getLocation();

        nearbyEntities.forEach(livingEntity -> {
            Location throwLocation = livingEntity.getEyeLocation();
            Vector vector = throwLocation.toVector().subtract(bossLocation.toVector()).normalize().multiply(finalMultiplier);

            livingEntity.setVelocity(vector);
        });
    }
}
