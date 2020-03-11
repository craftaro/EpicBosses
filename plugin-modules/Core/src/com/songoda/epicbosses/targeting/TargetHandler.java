package com.songoda.epicbosses.targeting;

import com.songoda.core.compatibility.ServerVersion;
import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.holder.IActiveHolder;
import com.songoda.epicbosses.managers.BossTargetManager;
import com.songoda.epicbosses.utils.ServerUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 30-Oct-18
 */
public abstract class TargetHandler<Holder extends IActiveHolder> implements ITarget {

    protected final BossTargetManager bossTargetManager;
    protected final Holder holder;

    public TargetHandler(Holder holder, BossTargetManager bossTargetManager) {
        this.holder = holder;
        this.bossTargetManager = bossTargetManager;
    }

    public void runTargetCycle() {
        ServerUtils.get().runLaterAsync(10L, () -> {
            updateTarget();

            if (!getHolder().isDead()) runTargetCycle();
        });
    }

    protected LivingEntity getBossEntity() {
        for (UUID uuid : getHolder().getLivingEntityMap().values()) {
            LivingEntity livingEntity = (LivingEntity) ServerUtils.get().getEntity(uuid);
            if (livingEntity != null && !livingEntity.isDead()) return livingEntity;
        }

        return null;
    }

    private void updateTarget() {
        Bukkit.getScheduler().runTask(EpicBosses.getInstance(), () -> {
            LivingEntity boss = getBossEntity();
            double radius = this.bossTargetManager.getTargetRadius();

            if (boss == null) return;

            List<LivingEntity> nearbyEntities = new ArrayList<>();
            List<Entity> nearbyBossEntities = boss.getNearbyEntities(radius, radius, radius);


            for (Entity entity : nearbyBossEntities) {
                if (!(entity instanceof Player)) continue;

                LivingEntity livingEntity = (LivingEntity) entity;

                Player player = (Player) livingEntity;

                if (player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE) continue;

                nearbyEntities.add(livingEntity);
            }

            updateBoss(selectTarget(nearbyEntities));
        });
    }

    private void updateBoss(LivingEntity newTarget) {
        getHolder().getLivingEntityMap().values().forEach(uuid -> {
            LivingEntity livingEntity = (LivingEntity) ServerUtils.get().getEntity(uuid);
            if (livingEntity != null && !livingEntity.isDead()) {
                if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_13) && livingEntity instanceof Mob)
                    ((Mob) livingEntity).setTarget(newTarget);
                else if (livingEntity instanceof Creature)
                    ((Creature) livingEntity).setTarget(newTarget);
            }
        });
    }

    public BossTargetManager getBossTargetManager() {
        return this.bossTargetManager;
    }

    public Holder getHolder() {
        return this.holder;
    }
}
