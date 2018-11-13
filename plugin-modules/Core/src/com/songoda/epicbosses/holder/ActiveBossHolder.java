package com.songoda.epicbosses.holder;

import lombok.Getter;
import lombok.Setter;
import com.songoda.epicbosses.targeting.TargetHandler;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.exception.AlreadySetException;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.*;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 03-Jun-18
 */
public class ActiveBossHolder {

    @Getter private final BossEntity bossEntity;
    @Getter private final Location location;
    @Getter private final String name;

    @Getter private Map<Integer, LivingEntity> livingEntityMap = new HashMap<>(), minionEntityMap = new HashMap<>();
    @Getter private Map<UUID, Double> mapOfDamagingUsers = new HashMap<>();

    @Getter @Setter private TargetHandler targetHandler = null;
    @Getter @Setter private boolean isDead = false;

    public ActiveBossHolder(BossEntity bossEntity, Location spawnLocation, String name) {
        this.location = spawnLocation;
        this.bossEntity = bossEntity;
        this.name = name;
    }

    public void setLivingEntity(int position, LivingEntity livingEntity) {
        if(getLivingEntityMap().containsKey(position)) {
            throw new AlreadySetException("Tried to set a new LivingEntity while it's already set.");
        } else {
            this.livingEntityMap.put(position, livingEntity);
        }
    }

    public LivingEntity getLivingEntity() {
        for(LivingEntity livingEntity : getLivingEntityMap().values()) {
            if(livingEntity != null) return livingEntity;
        }

        return null;
    }

    public boolean hasAttacked(UUID uuid) {
        return this.mapOfDamagingUsers.containsKey(uuid);
    }
}
