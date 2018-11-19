package com.songoda.epicbosses.targeting;

import com.songoda.epicbosses.holder.IActiveHolder;
import com.songoda.epicbosses.managers.BossTargetManager;
import com.songoda.epicbosses.utils.ServerUtils;
import lombok.Getter;
import org.bukkit.GameMode;
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
public abstract class TargetHandler<Holder extends IActiveHolder> implements ITarget {

    @Getter protected final BossTargetManager bossTargetManager;
    @Getter protected final Holder holder;

    public TargetHandler(Holder holder, BossTargetManager bossTargetManager) {
        this.holder = holder;
        this.bossTargetManager = bossTargetManager;
    }

    public void runTargetCycle() {
        ServerUtils.get().runLaterAsync(10L, () -> {
            updateTarget();

            if(!getHolder().isDead()) runTargetCycle();
        });
    }

    protected LivingEntity getBossEntity() {
        for(LivingEntity livingEntity : getHolder().getLivingEntityMap().values()) {
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

            if(livingEntity instanceof Player) {
                Player player = (Player) livingEntity;

                if(player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE) continue;
            }

            nearbyEntities.add(livingEntity);
        }

        updateBoss(selectTarget(nearbyEntities));
    }

    private void updateBoss(LivingEntity newTarget) {
        getHolder().getLivingEntityMap().values().forEach(livingEntity -> {
            if(livingEntity != null && !livingEntity.isDead()) {
                ((Creature) livingEntity).setTarget(newTarget);
            }
        });
    }

}
