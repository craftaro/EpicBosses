package com.songoda.epicbosses.targeting.types;

import com.songoda.epicbosses.holder.IActiveHolder;
import com.songoda.epicbosses.managers.BossTargetManager;
import com.songoda.epicbosses.targeting.TargetHandler;
import org.bukkit.entity.LivingEntity;

import java.util.Collections;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Nov-18
 */
public class NotDamagedNearbyTargetHandler<T extends IActiveHolder> extends TargetHandler<T> {

    public NotDamagedNearbyTargetHandler(T holder, BossTargetManager bossTargetManager) {
        super(holder, bossTargetManager);
    }

    @Override
    public LivingEntity selectTarget(List<LivingEntity> nearbyEntities) {
        for (LivingEntity livingEntity : nearbyEntities) {
            if (getHolder().hasAttacked(livingEntity.getUniqueId())) continue;

            return livingEntity;
        }

        if (!nearbyEntities.isEmpty()) {
            Collections.shuffle(nearbyEntities);

            return nearbyEntities.stream().findFirst().orElse(null);
        }

        return null;
    }
}
