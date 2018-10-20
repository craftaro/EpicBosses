package net.aminecraftdev.custombosses.listeners.during;

import net.aminecraftdev.custombosses.CustomBosses;
import net.aminecraftdev.custombosses.events.BossDamageEvent;
import net.aminecraftdev.custombosses.holder.ActiveBossHolder;
import net.aminecraftdev.custombosses.managers.BossEntityManager;
import net.aminecraftdev.custombosses.utils.ServerUtils;
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

    public BossDamageListener(CustomBosses plugin) {
        this.bossEntityManager = plugin.getBossEntityManager();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBossDamage(EntityDamageByEntityEvent event) {
        Entity entityBeingDamaged = event.getEntity();
        Entity entityDamaging = event.getDamager();

        if(!(entityBeingDamaged instanceof LivingEntity)) return;

        LivingEntity livingEntity = (LivingEntity) entityBeingDamaged;
        ActiveBossHolder activeBossHolder = this.bossEntityManager.getActiveBossHolder(livingEntity);
        double damage = event.getDamage();
        Player player = null;

        if(activeBossHolder == null) return;

        if(entityDamaging instanceof Player) {
            player = (Player) entityDamaging;
        } else if(entityDamaging instanceof Projectile) {
            Projectile projectile = (Projectile) entityDamaging;
            LivingEntity shooter = (LivingEntity) projectile.getShooter();

            if(projectile instanceof ThrownPotion) {
                event.setCancelled(true);
                return;
            }
            if(!(shooter instanceof Player)) return;

            player = (Player) shooter;
        }

        if(player == null) return;

        double currentDamage = activeBossHolder.getMapOfDamagingUsers().getOrDefault(player.getUniqueId(), 0.0);
        BossDamageEvent bossDamageEvent = new BossDamageEvent(activeBossHolder, livingEntity, livingEntity.getEyeLocation(), damage);

        ServerUtils.get().callEvent(bossDamageEvent);
        activeBossHolder.getMapOfDamagingUsers().put(player.getUniqueId(), currentDamage+damage);
    }

}
