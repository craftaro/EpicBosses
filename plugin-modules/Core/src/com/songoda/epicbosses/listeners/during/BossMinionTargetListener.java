package com.songoda.epicbosses.listeners.during;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.holder.ActiveMinionHolder;
import com.songoda.epicbosses.managers.BossEntityManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

/**
 * @author Esophose
 * @version 1.0.0
 * @since 05-Apr-19
 */
public class BossMinionTargetListener implements Listener {

    private BossEntityManager bossEntityManager;

    public BossMinionTargetListener(EpicBosses plugin) {
        this.bossEntityManager = plugin.getBossEntityManager();
    }

    @EventHandler
    public void onBossMinionTarget(EntityTargetLivingEntityEvent event) {
        Entity entityTargeting = event.getEntity();
        LivingEntity entityTargeted = event.getTarget();

        if (entityTargeted == null) return;
        if (!(entityTargeting instanceof LivingEntity)) return;

        LivingEntity livingEntity = (LivingEntity) entityTargeting;

        ActiveBossHolder targetingBossHolder = this.bossEntityManager.getActiveBossHolder(livingEntity);
        ActiveBossHolder targetedBossHolder = this.bossEntityManager.getActiveBossHolder(entityTargeted);

        if (targetingBossHolder != null) {
            for (ActiveMinionHolder minionHolder : targetingBossHolder.getActiveMinionHolderMap().values()) {
                if (minionHolder.getLivingEntityMap().containsValue(entityTargeted.getUniqueId())) {
                    event.setCancelled(true);
                    return;
                }
            }
        } else if (targetedBossHolder != null) {
            for (ActiveMinionHolder minionHolder : targetedBossHolder.getActiveMinionHolderMap().values()) {
                if (minionHolder.getLivingEntityMap().containsValue(entityTargeting.getUniqueId())) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

}
