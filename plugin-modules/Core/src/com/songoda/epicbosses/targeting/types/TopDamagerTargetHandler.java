package com.songoda.epicbosses.targeting.types;

import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.managers.BossTargetManager;
import com.songoda.epicbosses.targeting.TargetHandler;
import com.songoda.epicbosses.utils.MapUtils;
import org.bukkit.entity.LivingEntity;

import java.util.*;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 30-Oct-18
 */
public class TopDamagerTargetHandler extends TargetHandler {

    public TopDamagerTargetHandler(ActiveBossHolder activeBossHolder, BossTargetManager bossTargetManager) {
        super(activeBossHolder, bossTargetManager);
    }

    @Override
    public LivingEntity selectTarget(List<LivingEntity> nearbyEntities) {
        Map<LivingEntity, Double> nearbyDamages = new HashMap<>();
        Map<UUID, Double> mapOfDamages = getActiveBossHolder().getMapOfDamagingUsers();

        nearbyEntities.forEach(livingEntity -> {
            UUID uuid = livingEntity.getUniqueId();

            if(mapOfDamages.containsKey(uuid)) {
                nearbyDamages.put(livingEntity, mapOfDamages.get(uuid));
            }
        });

        Map<LivingEntity, Double> sortedMap = MapUtils.get().sortByValue(nearbyDamages);

        return sortedMap.keySet().stream().filter(livingEntity1 -> livingEntity1 != null && livingEntity1.isDead()).findFirst().orElse(null);
    }
}
