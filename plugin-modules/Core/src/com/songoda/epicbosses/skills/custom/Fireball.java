package com.songoda.epicbosses.skills.custom;

import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.skills.CustomSkillHandler;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.types.CustomSkillElement;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Nov-18
 */
public class Fireball extends CustomSkillHandler {

    @Override
    public boolean doesUseMultiplier() {
        return true;
    }

    @Override
    public Map<String, Class<?>> getOtherSkillData() {
        return null;
    }

    @Override
    public Map<Integer, ClickAction> getOtherSkillDataActions(Skill skill, CustomSkillElement customSkillElement) {
        return null;
    }

    @Override
    public void castSkill(Skill skill, CustomSkillElement customSkillElement, ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities) {
        LivingEntity boss = activeBossHolder.getLivingEntity();

        if(boss == null) return;

        Double multiplier = customSkillElement.getCustom().getMultiplier();

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
