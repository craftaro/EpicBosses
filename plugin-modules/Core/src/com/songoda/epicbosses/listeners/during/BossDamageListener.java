package com.songoda.epicbosses.listeners.during;

import com.songoda.core.utils.TextUtils;
import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.events.BossDamageEvent;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.managers.BossEntityManager;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.utils.ServerUtils;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 20-Oct-18
 */
public class BossDamageListener implements Listener {

    private BossEntityManager bossEntityManager;
    private BossesFileManager bossesFileManager;

    public BossDamageListener(EpicBosses plugin) {
        this.bossEntityManager = plugin.getBossEntityManager();
        this.bossesFileManager = plugin.getBossesFileManager();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBossDamage(EntityDamageByEntityEvent event) {
        Entity entityBeingDamaged = event.getEntity();
        Entity entityDamaging = event.getDamager();

        if (!(entityBeingDamaged instanceof LivingEntity)) return;

        LivingEntity livingEntity = (LivingEntity) entityBeingDamaged;
        ActiveBossHolder activeBossHolder = this.bossEntityManager.getActiveBossHolder(livingEntity);
        double damage = event.getDamage();
        Player player = null;

        if (activeBossHolder == null) {
            // Check to see if this was a boss and respawn it if so.
            String convert = TextUtils.convertFromInvisibleString(livingEntity.getCustomName());
            if (convert.startsWith("BOSS:")) {
                String name = convert.split(":")[1];

                BossEntity bossEntity = bossesFileManager.getBossEntity(name);
                bossEntityManager.createActiveBossHolder(bossEntity, livingEntity.getLocation(), name, null);
                livingEntity.remove();
            }
            return;
        }

        if (entityDamaging instanceof Player) {
            player = (Player) entityDamaging;
        } else if (entityDamaging instanceof Projectile) {
            Projectile projectile = (Projectile) entityDamaging;
            LivingEntity shooter = (LivingEntity) projectile.getShooter();

            if (projectile instanceof ThrownPotion) {
                event.setCancelled(true);
                return;
            }
            if (!(shooter instanceof Player)) return;

            player = (Player) shooter;
        }

        if (player == null) return;

        double currentDamage = activeBossHolder.getMapOfDamagingUsers().getOrDefault(player.getUniqueId(), 0.0);
        BossDamageEvent bossDamageEvent = new BossDamageEvent(activeBossHolder, livingEntity, livingEntity.getEyeLocation(), damage);

        ServerUtils.get().callEvent(bossDamageEvent);
        activeBossHolder.getMapOfDamagingUsers().put(player.getUniqueId(), currentDamage + damage);
    }

}
