package com.songoda.epicbosses.managers;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.targetting.TargetHandler;
import com.songoda.epicbosses.targetting.types.ClosestTargetHandler;
import com.songoda.epicbosses.targetting.types.NotDamagedNearbyTargetHandler;
import com.songoda.epicbosses.targetting.types.RandomNearbyTargetHandler;
import com.songoda.epicbosses.targetting.types.TopDamagerTargetHandler;
import lombok.Getter;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 03-Nov-18
 */
public class BossTargetManager {

    @Getter private final CustomBosses plugin;

    public BossTargetManager(CustomBosses plugin) {
        this.plugin = plugin;
    }

    public double getTargetRadius() {
        return this.plugin.getConfig().getDouble("Settings.bossTargetRange", 50.0);
    }

    public void initializeTargetHandler(ActiveBossHolder activeBossHolder) {
        BossEntity bossEntity = activeBossHolder.getBossEntity();
        String targeting = bossEntity.getTargeting();
        TargetHandler targetHandler;

        if(targeting.equalsIgnoreCase("RandomNearby")) {
           targetHandler = getRandomNearbyTargetHandler(activeBossHolder);
        } else if(targeting.equalsIgnoreCase("TopDamager")) {
            targetHandler = getTopDamagerTargetHandler(activeBossHolder);
        } else if(targeting.equalsIgnoreCase("NotDamagedNearby")) {
            targetHandler = getNotDamagedNearbyTargetHandler(activeBossHolder);
        } else {
            targetHandler = getClosestTargetHandler(activeBossHolder);
        }

        activeBossHolder.setTargetHandler(targetHandler);
    }

    private TargetHandler getClosestTargetHandler(ActiveBossHolder activeBossHolder) {
        return new ClosestTargetHandler(activeBossHolder, this);
    }

    private TargetHandler getNotDamagedNearbyTargetHandler(ActiveBossHolder activeBossHolder) {
        return new NotDamagedNearbyTargetHandler(activeBossHolder, this);
    }

    private TargetHandler getRandomNearbyTargetHandler(ActiveBossHolder activeBossHolder) {
        return new RandomNearbyTargetHandler(activeBossHolder, this);
    }

    private TargetHandler getTopDamagerTargetHandler(ActiveBossHolder activeBossHolder) {
        return new TopDamagerTargetHandler(activeBossHolder, this);
    }

}
