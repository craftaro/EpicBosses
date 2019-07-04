package com.songoda.epicbosses.holder;

import com.songoda.epicbosses.listeners.IBossDeathHandler;
import com.songoda.epicbosses.utils.ServerUtils;
import lombok.Getter;
import lombok.Setter;
import com.songoda.epicbosses.targeting.TargetHandler;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.exception.AlreadySetException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 03-Jun-18
 */
public class ActiveBossHolder implements IActiveHolder {

    @Getter private final BossEntity bossEntity;
    @Getter private final Location location;
    @Getter private final String name;

    @Getter private Map<Integer, ActiveMinionHolder> activeMinionHolderMap = new HashMap<>();
    @Getter private Map<Integer, UUID> livingEntityMap = new HashMap<>();
    @Getter private List<IBossDeathHandler> postBossDeathHandlers = new ArrayList<>();
    @Getter private Map<UUID, Double> mapOfDamagingUsers = new HashMap<>();
    @Getter private String spawningPlayerName;

    @Getter @Setter private TargetHandler<ActiveBossHolder> targetHandler = null;
    @Getter @Setter private boolean isDead = false, customSpawnMessage = false;

    public ActiveBossHolder(BossEntity bossEntity, Location spawnLocation, String name, Player spawningPlayer) {
        this.location = spawnLocation;
        this.bossEntity = bossEntity;
        this.name = name;

        if (spawningPlayer != null) {
            this.spawningPlayerName = spawningPlayer.getName();
        }
    }

    public void setLivingEntity(int position, LivingEntity livingEntity) {
        if (this.livingEntityMap.containsKey(position)) {
            throw new AlreadySetException("Tried to set a new LivingEntity while it's already set.");
        } else {
            this.livingEntityMap.put(position, livingEntity.getUniqueId());
        }
    }

    @Override
    public void killAll() {
        this.killAllMinions();
        this.killAllSubBosses(null);
    }

    public LivingEntity getLivingEntity() {
        return this.getLivingEntity(1);
    }

    @Override
    public LivingEntity getLivingEntity(int position) {
        UUID target = this.livingEntityMap.get(position);
        if (target == null)
            return null;
        return (LivingEntity) ServerUtils.get().getEntity(target);
    }

    public boolean hasAttacked(UUID uuid) {
        return this.mapOfDamagingUsers.containsKey(uuid);
    }

    public void killAllMinions() {
        this.activeMinionHolderMap.values().forEach(ActiveMinionHolder::killAll);
    }

    public void killAllMinions(World world) {
        if (world != null && !this.location.getWorld().equals(world))
            return;

        this.activeMinionHolderMap.values().forEach(ActiveMinionHolder::killAll);
    }

    public boolean killAllSubBosses(World world) {
        if (world != null && !this.location.getWorld().equals(world))
            return false;

        this.livingEntityMap.values().forEach(e -> {
            Entity entity = ServerUtils.get().getEntity(e);
            if (entity != null)
                entity.remove();
        });

        this.livingEntityMap.clear();
        return true;
    }
}
