package com.songoda.epicbosses.skills.custom;

import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.skills.CustomSkillHandler;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.types.CustomSkillElement;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Nov-18
 */
public class Grapple extends CustomSkillHandler {

    @Override
    public boolean doesUseMultiplier() {
        return true;
    }

    @Override
    public Map<String, Class<?>> getOtherSkillData() {
        return null;
    }

    @Override
    public void castSkill(Skill skill, CustomSkillElement customSkillElement, ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities) {
        Location bossLocation = activeBossHolder.getLocation();
        Double multiplier = customSkillElement.getCustom().getMultiplier();

        if(multiplier == null) multiplier = 1.0;

        double finalMultiplier = multiplier;

        nearbyEntities.forEach(livingEntity -> {
            Location location = livingEntity.getLocation();
            Vector vector = location.toVector().subtract(bossLocation.toVector()).normalize().multiply(finalMultiplier);

            livingEntity.setVelocity(vector);
        });
    }
}
