package com.songoda.epicbosses.holder;

import com.songoda.epicbosses.entity.BossEntity;
import org.bukkit.Location;

import java.util.Map;
import java.util.UUID;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Oct-18
 */
public class DeadBossHolder {

    private final Map<UUID, Double> sortedDamageMap, percentageMap;
    private final BossEntity bossEntity;
    private final Location location;

    public DeadBossHolder(BossEntity bossEntity, Location deathLocation, Map<UUID, Double> sortedDamageMap, Map<UUID, Double> percentageMap) {
        this.location = deathLocation;
        this.bossEntity = bossEntity;
        this.sortedDamageMap = sortedDamageMap;
        this.percentageMap = percentageMap;
    }

    public Map<UUID, Double> getSortedDamageMap() {
        return this.sortedDamageMap;
    }

    public Map<UUID, Double> getPercentageMap() {
        return this.percentageMap;
    }

    public BossEntity getBossEntity() {
        return this.bossEntity;
    }

    public Location getLocation() {
        return this.location;
    }
}
