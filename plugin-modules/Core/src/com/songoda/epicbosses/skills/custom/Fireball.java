package com.songoda.epicbosses.skills.custom;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.skills.types.CustomSkill;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Nov-18
 */
public class Fireball extends CustomSkill {

    public Fireball(CustomBosses plugin) {
        super(plugin);
    }

    @Override
    public void castSkill(ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities) {
        LivingEntity boss = activeBossHolder.getLivingEntity();

        if(boss == null) return;

        Double multiplier = getCustom().getMultiplier();

        if(multiplier == null) multiplier = 1.0;

        double finalMultiplier = multiplier;

        nearbyEntities.forEach(livingEntity -> {
            Vector vector = livingEntity.getLocation().subtract(activeBossHolder.getLocation()).toVector().normalize().multiply(finalMultiplier);
            org.bukkit.entity.Fireball fireball = livingEntity.getWorld().spawn(activeBossHolder.getLocation(), org.bukkit.entity.Fireball.class);

            fireball.setShooter(boss);
            fireball.setDirection(vector);
            fireball.setIsIncendiary(false);
        });
    }
}
