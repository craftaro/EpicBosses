package com.songoda.epicbosses.targetting.types;

import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.managers.BossTargetManager;
import com.songoda.epicbosses.targetting.TargetHandler;
import org.bukkit.entity.LivingEntity;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 30-Oct-18
 */
public class ClosestTargetHandler extends TargetHandler {

    public ClosestTargetHandler(ActiveBossHolder activeBossHolder, BossTargetManager bossTargetManager) {
        super(activeBossHolder, bossTargetManager);
    }

    @Override
    public LivingEntity selectTarget(List<LivingEntity> nearbyEntities) {
        LivingEntity boss = getBossEntity();
        double radius = this.bossTargetManager.getTargetRadius();
        double closestDistance = (radius * radius);
        LivingEntity nearestTarget = null;

        for(LivingEntity livingEntity : nearbyEntities) {
            if(livingEntity.getLocation().distanceSquared(boss.getLocation()) > closestDistance) continue;

            closestDistance = livingEntity.getLocation().distanceSquared(boss.getLocation());
            nearestTarget = livingEntity;
        }

        return nearestTarget;
    }
}
