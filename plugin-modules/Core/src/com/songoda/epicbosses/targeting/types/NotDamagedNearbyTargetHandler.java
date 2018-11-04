package com.songoda.epicbosses.targeting.types;

import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.managers.BossTargetManager;
import com.songoda.epicbosses.targeting.TargetHandler;
import org.bukkit.entity.LivingEntity;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Nov-18
 */
public class NotDamagedNearbyTargetHandler extends TargetHandler {

    public NotDamagedNearbyTargetHandler(ActiveBossHolder activeBossHolder, BossTargetManager bossTargetManager) {
        super(activeBossHolder, bossTargetManager);
    }

    @Override
    public LivingEntity selectTarget(List<LivingEntity> nearbyEntities) {
        for(LivingEntity livingEntity : nearbyEntities) {
            if(getActiveBossHolder().hasAttacked(livingEntity.getUniqueId())) continue;

            return livingEntity;
        }

        return null;
    }
}
