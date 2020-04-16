package com.songoda.epicbosses.listeners.during;

import com.songoda.core.utils.TextUtils;
import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.events.BossDamageEvent;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.managers.BossEntityManager;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.settings.Settings;
import com.songoda.epicbosses.utils.ServerUtils;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

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

        if (activeBossHolder == null && livingEntity.getCustomName() != null) {
            // Check to see if this was a boss and respawn it if so.
            String convert = livingEntity.getCustomName();

            if (convert.toUpperCase().startsWith(TextUtils.convertToInvisibleString("BOSS:"))) {
                String name = convert.split(":")[1];

                BossEntity bossEntity = bossesFileManager.getBossEntity(name);
                if (bossEntity != null) {
                    bossEntityManager.createActiveBossHolder(bossEntity, livingEntity.getLocation(), name, null);

                    if (livingEntity.isInsideVehicle() && livingEntity.getVehicle() != null)
                        livingEntity.getVehicle().remove();

                    if (livingEntity.getPassenger() != null)
                        livingEntity.getPassenger().remove();

                    livingEntity.remove();
                }
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

        if (player == null || activeBossHolder == null) return;

        double currentDamage = activeBossHolder.getMapOfDamagingUsers().getOrDefault(player.getUniqueId(), 0.0);
        BossDamageEvent bossDamageEvent = new BossDamageEvent(activeBossHolder, livingEntity, livingEntity.getEyeLocation(), damage);

        ServerUtils.get().callEvent(bossDamageEvent);
        activeBossHolder.getMapOfDamagingUsers().put(player.getUniqueId(), currentDamage + damage);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBossExplode(ExplosionPrimeEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity livingEntity = (LivingEntity) event.getEntity();
        ActiveBossHolder activeBossHolder = this.bossEntityManager.getActiveBossHolder(livingEntity);

        if (activeBossHolder != null && !Settings.BOSS_EXPLOSIONS.getBoolean())
            event.setCancelled(true);
    }
}
