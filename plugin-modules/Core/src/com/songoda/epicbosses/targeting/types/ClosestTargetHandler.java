package com.songoda.epicbosses.targeting.types;

import com.songoda.epicbosses.holder.IActiveHolder;
import com.songoda.epicbosses.managers.BossTargetManager;
import com.songoda.epicbosses.targeting.TargetHandler;
import org.bukkit.entity.LivingEntity;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 30-Oct-18
 */
public class ClosestTargetHandler<T extends IActiveHolder> extends TargetHandler<T> {

    public ClosestTargetHandler(T holder, BossTargetManager bossTargetManager) {
        super(holder, bossTargetManager);
    }

    @Override
    public LivingEntity selectTarget(List<LivingEntity> nearbyEntities) {
        LivingEntity boss = getBossEntity();
        double radius = this.bossTargetManager.getTargetRadius();
        double closestDistance = (radius * radius);
        LivingEntity nearestTarget = null;

        for (LivingEntity livingEntity : nearbyEntities) {
            if (livingEntity.getLocation().distanceSquared(boss.getLocation()) > closestDistance) continue;

            closestDistance = livingEntity.getLocation().distanceSquared(boss.getLocation());
            nearestTarget = livingEntity;
        }

        return nearestTarget;
    }
}
