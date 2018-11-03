package com.songoda.epicbosses.targetting;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.managers.BossTargetManager;
import com.songoda.epicbosses.utils.ServerUtils;
import lombok.Getter;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 30-Oct-18
 */
public abstract class TargetHandler implements ITarget {

    @Getter protected final BossTargetManager bossTargetManager;
    @Getter protected final ActiveBossHolder activeBossHolder;

    public TargetHandler(ActiveBossHolder activeBossHolder, BossTargetManager bossTargetManager) {
        this.activeBossHolder = activeBossHolder;
        this.bossTargetManager = bossTargetManager;
    }

    public void runTargetCycle() {
        ServerUtils.get().runLaterAsync(100L, () -> {
            updateTarget();

            if(!getActiveBossHolder().isDead()) runTargetCycle();
        });
    }

    protected LivingEntity getBossEntity() {
        for(LivingEntity livingEntity : this.activeBossHolder.getLivingEntityMap().values()) {
            if(livingEntity != null && !livingEntity.isDead()) return livingEntity;
        }

        return null;
    }

    private void updateTarget() {
        LivingEntity boss = getBossEntity();
        double radius = this.bossTargetManager.getTargetRadius();

        if(boss == null) return;

        List<LivingEntity> nearbyEntities = new ArrayList<>();

        for(Entity entity : boss.getNearbyEntities(radius, radius, radius)) {
            if(!(entity instanceof Player)) continue;

            LivingEntity livingEntity = (LivingEntity) entity;

            nearbyEntities.add(livingEntity);
        }

        updateBoss(selectTarget(nearbyEntities));
    }

    private void updateBoss(LivingEntity newTarget) {
        this.activeBossHolder.getLivingEntityMap().values().forEach(livingEntity -> {
            if(livingEntity != null && !livingEntity.isDead()) {
                ((Creature) livingEntity).setTarget(newTarget);
            }
        });
    }

}
