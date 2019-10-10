package com.songoda.epicbosses.managers;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.MinionEntity;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.holder.ActiveMinionHolder;
import com.songoda.epicbosses.holder.IActiveHolder;
import com.songoda.epicbosses.targeting.TargetHandler;
import com.songoda.epicbosses.targeting.types.ClosestTargetHandler;
import com.songoda.epicbosses.targeting.types.NotDamagedNearbyTargetHandler;
import com.songoda.epicbosses.targeting.types.RandomNearbyTargetHandler;
import com.songoda.epicbosses.targeting.types.TopDamagerTargetHandler;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 03-Nov-18
 */
public class BossTargetManager {

    private final EpicBosses plugin;

    public BossTargetManager(EpicBosses plugin) {
        this.plugin = plugin;
    }

    public double getTargetRadius() {
        return this.plugin.getConfig().getDouble("Settings.bossTargetRange", 50.0);
    }

    public void handleBossTargeting(ActiveBossHolder activeBossHolder) {
        BossEntity bossEntity = activeBossHolder.getBossEntity();
        String targeting = bossEntity.getTargeting();
        TargetHandler<ActiveBossHolder> targetHandler;

        if (targeting.equalsIgnoreCase("RandomNearby")) {
            targetHandler = getRandomNearbyTargetHandler(activeBossHolder);
        } else if (targeting.equalsIgnoreCase("TopDamager")) {
            targetHandler = getTopDamagerTargetHandler(activeBossHolder);
        } else if (targeting.equalsIgnoreCase("NotDamagedNearby")) {
            targetHandler = getNotDamagedNearbyTargetHandler(activeBossHolder);
        } else {
            targetHandler = getClosestTargetHandler(activeBossHolder);
        }

        activeBossHolder.setTargetHandler(targetHandler);
    }

    public void handleMinionTargeting(ActiveMinionHolder activeMinionHolder) {
        MinionEntity minionEntity = activeMinionHolder.getMinionEntity();
        String targeting = minionEntity.getTargeting();
        TargetHandler<ActiveMinionHolder> targetHandler;

        if (targeting.equalsIgnoreCase("RandomNearby")) {
            targetHandler = getRandomNearbyTargetHandler(activeMinionHolder);
        } else if (targeting.equalsIgnoreCase("TopDamager")) {
            targetHandler = getTopDamagerTargetHandler(activeMinionHolder);
        } else if (targeting.equalsIgnoreCase("NotDamagedNearby")) {
            targetHandler = getNotDamagedNearbyTargetHandler(activeMinionHolder);
        } else {
            targetHandler = getClosestTargetHandler(activeMinionHolder);
        }

        activeMinionHolder.setTargetHandler(targetHandler);
    }

    private <T extends IActiveHolder> TargetHandler<T> getClosestTargetHandler(T holder) {
        return new ClosestTargetHandler<>(holder, this);
    }

    private <T extends IActiveHolder> TargetHandler<T> getNotDamagedNearbyTargetHandler(T holder) {
        return new NotDamagedNearbyTargetHandler<>(holder, this);
    }

    private <T extends IActiveHolder> TargetHandler<T> getRandomNearbyTargetHandler(T holder) {
        return new RandomNearbyTargetHandler<>(holder, this);
    }

    private <T extends IActiveHolder> TargetHandler<T> getTopDamagerTargetHandler(T holder) {
        return new TopDamagerTargetHandler<>(holder, this);
    }

    public EpicBosses getPlugin() {
        return this.plugin;
    }
}
