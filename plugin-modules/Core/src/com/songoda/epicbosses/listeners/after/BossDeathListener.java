package com.songoda.epicbosses.listeners.after;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.events.BossDeathEvent;
import com.songoda.epicbosses.events.PreBossDeathEvent;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.holder.DeadBossHolder;
import com.songoda.epicbosses.managers.BossEntityManager;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.ServerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.*;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 20-Oct-18
 */
public class BossDeathListener implements Listener {

    private BossEntityManager bossEntityManager;


    public BossDeathListener(CustomBosses plugin) {
        this.bossEntityManager = plugin.getBossEntityManager();
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        LivingEntity livingEntity = event.getEntity();
        EntityDamageEvent entityDamageEvent = livingEntity.getLastDamageCause();
        ActiveBossHolder activeBossHolder = this.bossEntityManager.getActiveBossHolder(livingEntity);
        Location location = livingEntity.getLocation();

        if(activeBossHolder == null) return;

        EntityDamageEvent.DamageCause damageCause = entityDamageEvent.getCause();

        if(damageCause == EntityDamageEvent.DamageCause.VOID || damageCause == EntityDamageEvent.DamageCause.LAVA
                || activeBossHolder.getMapOfDamagingUsers().isEmpty()) {
            this.bossEntityManager.removeActiveBossHolder(activeBossHolder);
            return;
        }

        if(this.bossEntityManager.isAllEntitiesDead(activeBossHolder)) {
            PreBossDeathEvent preBossDeathEvent = new PreBossDeathEvent(activeBossHolder, location);

            ServerUtils.get().callEvent(preBossDeathEvent);
        }
    }

    @EventHandler
    public void onPreBossDeath(PreBossDeathEvent event) {
        ActiveBossHolder activeBossHolder = event.getActiveBossHolder();
        BossEntity bossEntity = activeBossHolder.getBossEntity();
        Location location = event.getLocation();

        Map<UUID, Double> mapOfDamage = this.bossEntityManager.getSortedMapOfDamage(activeBossHolder);
        List<String> commands = this.bossEntityManager.getOnDeathCommands(bossEntity);
        List<String> messages = this.bossEntityManager.getOnDeathMessage(bossEntity);
        int messageRadius = this.bossEntityManager.getOnDeathMessageRadius(bossEntity);
        int onlyShow = this.bossEntityManager.getOnDeathShowAmount(bossEntity);

        if(commands != null) {
            commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
        }

        ServerUtils.get().runTaskAsync(() -> {
            List<String> positionsMessage = this.bossEntityManager.getOnDeathPositionMessage(bossEntity);

            if(messages != null) {
                if(positionsMessage != null) {
                    List<String> finalPositionsMessage = new ArrayList<>();
                    int current = 1;

                    for(Map.Entry<UUID, Double> entry : mapOfDamage.entrySet()) {
                        if(current > onlyShow) break;

                        List<String> clonedPositionsMessage = new ArrayList<>(positionsMessage);
                        double percentage = this.bossEntityManager.getPercentage(activeBossHolder, entry.getKey());
                        int position = current;

                        clonedPositionsMessage.replaceAll(s -> s
                                .replace("{pos}", ""+position)
                                .replace("{name}", Bukkit.getOfflinePlayer(entry.getKey()).getName())
                                .replace("{dmg}", NumberUtils.get().formatDouble(entry.getValue()))
                                .replace("{percent}", NumberUtils.get().formatDouble(percentage))
                                .replace('&', '§'));

                        finalPositionsMessage.addAll(clonedPositionsMessage);
                        current++;
                    }

                    positionsMessage = finalPositionsMessage;
                }

                if(activeBossHolder.getName() != null) messages.replaceAll(s -> s.replace("{boss}", activeBossHolder.getName()));

                messages.replaceAll(s -> s.replace('&', '§'));

                List<String> finalMessage = new ArrayList<>();

                for(String s : messages) {
                    if(s.contains("{positions}") && positionsMessage != null) {
                        finalMessage.addAll(positionsMessage);
                    } else {
                        finalMessage.add(s);
                    }
                }

                if(messageRadius == -1) {
                    finalMessage.forEach(Bukkit::broadcastMessage);
                } else {
                    Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                        if(onlinePlayer.getWorld().getName().equals(location.getWorld().getName())) {
                            if(onlinePlayer.getLocation().distanceSquared(location) <= messageRadius) {
                                finalMessage.forEach(s -> onlinePlayer.sendMessage(s));
                            }
                        }
                    });
                }
            }
        });

        DeadBossHolder deadBossHolder = new DeadBossHolder(bossEntity, location, mapOfDamage);
        BossDeathEvent bossDeathEvent = new BossDeathEvent(activeBossHolder);
        DropTable dropTable = this.bossEntityManager.getDropTable(bossEntity);

        if(dropTable == null) {
            Debug.FAILED_TO_FIND_DROP_TABLE.debug(activeBossHolder.getName(), bossEntity.getDrops().getDropTable());
            return;
        }

        this.bossEntityManager.handleDropTable(dropTable, deadBossHolder);
        ServerUtils.get().callEvent(bossDeathEvent);
    }

}
