package com.songoda.epicbosses.targeting.types;

import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.managers.BossTargetManager;
import com.songoda.epicbosses.targeting.TargetHandler;
import org.bukkit.entity.LivingEntity;

import java.util.Collections;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 30-Oct-18
 */
public class RandomNearbyTargetHandler extends TargetHandler {

    public RandomNearbyTargetHandler(ActiveBossHolder activeBossHolder, BossTargetManager bossTargetManager) {
        super(activeBossHolder, bossTargetManager);
    }

    @Override
    public LivingEntity selectTarget(List<LivingEntity> nearbyEntities) {
        Collections.shuffle(nearbyEntities);

        return nearbyEntities.stream().findFirst().orElse(null);
    }
}
